package com.taurus.common.aop;

import com.alibaba.fastjson.JSONObject;
import com.taurus.common.constant.CommonConstant;
import com.taurus.common.enums.ExceptionEnum;
import com.taurus.common.model.BusinessException;
import com.taurus.common.model.SysRequestLog;
import com.taurus.common.model.SysResponseLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * api日志
 *
 * @author 郑楷山
 **/

@Slf4j
@Aspect
@Component
public class RequestLogAspect {

	private static final ThreadLocal<SysRequestLog> requestLogThreadLocal = new ThreadLocal<>();

	@Value("${spring.application.name}")
	private String applicationName;

	@Pointcut("execution(* com..controller..*(..))")
	public void servicePointcut() {
	}

	@Before("servicePointcut()")
	public void before(JoinPoint joinPoint) {
		try {
			HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
			String uri = request.getRequestURI();
			String requestId = UUID.randomUUID().toString();
			assert response != null;
			response.setHeader("X-Request-ID", requestId);
			String ipAddress = StringUtils.isBlank(request.getHeader(CommonConstant.X_REAL_IP)) ? request.getRemoteAddr() : request.getHeader(CommonConstant.X_REAL_IP);
			ipAddress = StringUtils.isBlank(request.getHeader(CommonConstant.X_FORWARDED_FOR)) ? ipAddress : request.getHeader(CommonConstant.X_FORWARDED_FOR).split(",")[0];
			Object[] args = joinPoint.getArgs();
			if (args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					if (args[i] instanceof MultipartFile
							|| args[i] instanceof ServletRequest
							|| args[i] instanceof ServletResponse
							|| args[i] instanceof InputStream
							|| args[i] instanceof OutputStream) {
						args[i] = args[i].toString();
					}
				}
			}
			String methodName = joinPoint.getSignature().getDeclaringTypeName()
												+ "."
												+ joinPoint.getSignature().getName();
			SysRequestLog sysRequestLog = new SysRequestLog()
					.setRequestTime(System.currentTimeMillis())
					.setRequestIPAddress(ipAddress.trim())
					.setRequestUri(uri)
					.setParameter(args)
					.setMethodName(methodName)
					.setRequestId(requestId)
					.setProcessServiceName(applicationName);
			requestLogThreadLocal.set(sysRequestLog);
			log.info(CommonConstant.LOG_REQUEST_FORMAT, JSONObject.toJSONString(sysRequestLog));
		} catch (Throwable t) {
			requestLogThreadLocal.remove();
			log.error(CommonConstant.LOG_EXCEPTION_BEFORE, t);
		}
	}

	@AfterReturning(returning = "resultValue", pointcut = "servicePointcut()")
	public void after(Object resultValue) {
		try {
			SysRequestLog sysRequestLog = requestLogThreadLocal.get();
			SysResponseLog sysResponseLog = new SysResponseLog();
			sysResponseLog
					.setStatus(HttpStatus.OK.value())
					.setProcessTime(System.currentTimeMillis() - sysRequestLog.getRequestTime());
			log.info(CommonConstant.LOG_RESPONSE_FORMAT, JSONObject.toJSONString(sysResponseLog));
			requestLogThreadLocal.remove();
		} catch (Throwable t) {
			requestLogThreadLocal.remove();
			log.error(CommonConstant.LOG_EXCEPTION_AFTER_RETURNING, t);
		}
	}

	@AfterThrowing(throwing = "throwable", pointcut = "servicePointcut()")
	public void afterThrowing(Throwable throwable) {
		try {
			SysRequestLog sysRequestLog = requestLogThreadLocal.get();
			SysResponseLog sysResponseLog = new SysResponseLog();
			if (throwable instanceof BusinessException) {
				BusinessException businessException = (BusinessException) throwable;
				sysResponseLog.setStatus(businessException.getStatus())
						.setCode(businessException.getError())
						.setExceptionClassName(businessException.getCause()
								!= null
								? businessException.getCause().getClass().getName()
								: businessException.getClass().getName());
			} else {
				sysResponseLog.setStatus(ExceptionEnum.SERVER_ERROR.getStatus())
						.setCode(ExceptionEnum.SERVER_ERROR.getError())
						.setExceptionClassName(throwable.getClass().getName());
			}
			sysResponseLog.setProcessTime(System.currentTimeMillis() - sysRequestLog.getRequestTime());
			log.info(CommonConstant.LOG_RESPONSE_FORMAT, JSONObject.toJSONString(sysResponseLog));
			requestLogThreadLocal.remove();
		} catch (Throwable t) {
			requestLogThreadLocal.remove();
			log.error(CommonConstant.LOG_THROWING_AFTER_THROWING, t);
		}
	}

}
