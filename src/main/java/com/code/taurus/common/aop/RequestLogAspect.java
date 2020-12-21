package com.code.taurus.common.aop;

import com.alibaba.fastjson.JSONObject;
import com.code.taurus.common.constant.CommonConstant;
import com.code.taurus.common.enums.ExceptionEnum;
import com.code.taurus.common.model.BusinessException;
import com.code.taurus.common.model.SysRequestLog;
import com.code.taurus.common.model.SysResponseLog;
import com.code.taurus.common.util.RequestUtil;
import com.code.taurus.common.config.security.RequestLogContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
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

/**
 * api日志
 *
 * @author 郑楷山
 **/

@Slf4j
@Aspect
@Component
@Order(1)
public class RequestLogAspect {

//    @Value("${spring.application.name}")
//    private String applicationName;

    @Pointcut("execution(* com..controller..*(..))")
    public void controllerPointcut() {
    }

    @Before("controllerPointcut()")
    public void before(JoinPoint joinPoint) {
        try {
            ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            assert servletRequestAttributes != null;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpServletResponse response = servletRequestAttributes.getResponse();
            assert response != null;
            String uri = request.getRequestURI();
            String requestId = RequestUtil.buildRequestID();
            response.setHeader("X-Request-ID", requestId);
            String ipAddress = RequestUtil.getRealIp();
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
//			String methodName = joinPoint.getSignature().getDeclaringTypeName()
//												+ "."
//												+ joinPoint.getSignature().getName();
            SysRequestLog sysRequestLog = new SysRequestLog()
                    .setRequestTime(System.currentTimeMillis())
                    .setRequestIPAddress(ipAddress)
                    .setRequestUri(uri)
                    .setParameter(args)
                    .setRequestId(requestId);
//					 .setMethodName(methodName)
//					 .setProcessServiceName(applicationName)
            log.info(CommonConstant.LOG_REQUEST_FORMAT, requestId, JSONObject.toJSONString(sysRequestLog));
            RequestLogContextHelper.setRequestLog(sysRequestLog.setRequestId(requestId));
        } catch (Throwable t) {
            RequestLogContextHelper.remove();
            log.error(CommonConstant.LOG_EXCEPTION_BEFORE, t);
        }
    }

    @AfterReturning(returning = "resultValue", pointcut = "controllerPointcut()")
    public void after(Object resultValue) {
        try {
            SysRequestLog sysRequestLog = RequestLogContextHelper.getRequestLog().orElse(new SysRequestLog());
            SysResponseLog sysResponseLog = new SysResponseLog();
            sysResponseLog
                    .setStatus(HttpStatus.OK.value())
                    .setProcessTime(System.currentTimeMillis() - sysRequestLog.getRequestTime());
            log.info(CommonConstant.LOG_RESPONSE_FORMAT, sysRequestLog.getRequestId(), JSONObject.toJSONString(sysResponseLog));
        } catch (Throwable t) {
            log.error(CommonConstant.LOG_EXCEPTION_AFTER_RETURNING, t);
        } finally {
            RequestLogContextHelper.remove();
        }
    }

//    @AfterThrowing(throwing = "throwable", pointcut = "servicePointcut()")
    public void afterThrowing(Throwable throwable) {
        SysRequestLog sysRequestLog = RequestLogContextHelper.getRequestLog().orElse(new SysRequestLog());
        try {
            SysResponseLog sysResponseLog = new SysResponseLog();
            if (throwable instanceof BusinessException) {
                BusinessException businessException = (BusinessException) throwable;
                sysResponseLog.setStatus(businessException.getStatus())
                        .setMessage(businessException.getMessage())
                        .setExceptionClassName(businessException.getCause()
                                != null
                                ? businessException.getCause().getClass().getName()
                                : businessException.getClass().getName());
            } else {
                sysResponseLog.setStatus(ExceptionEnum.SERVER_ERROR.getStatus())
                        .setMessage(ExceptionEnum.SERVER_ERROR.getMessage())
                        .setExceptionClassName(throwable.getClass().getName());
            }
            sysResponseLog
                    .setProcessTime(System.currentTimeMillis() - sysRequestLog.getRequestTime())
                    .setRequestId(sysRequestLog.getRequestId());
            log.error(CommonConstant.LOG_RESPONSE_FORMAT, JSONObject.toJSONString(sysResponseLog));
        } catch (Throwable t) {
            log.error("请求ID:{" + sysRequestLog.getRequestId() + "};" + CommonConstant.LOG_THROWING_AFTER_THROWING, t);
        } finally {
            RequestLogContextHelper.remove();
        }
    }

}
