package com.code.taurus.common.config;

import com.alibaba.fastjson.JSONObject;
import com.code.taurus.common.config.security.RequestLogContextHelper;
import com.code.taurus.common.constant.CommonConstant;
import com.code.taurus.common.enums.ExceptionEnum;
import com.code.taurus.common.model.BusinessException;
import com.code.taurus.common.model.SysExceptionJson;
import com.code.taurus.common.model.SysRequestLog;
import com.code.taurus.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 异常处理
 *
 * @author 郑楷山
 **/

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(Exception.class)
    public JSONObject handleException(Exception exception,
                                      HttpServletResponse response) {
        BusinessException businessException = null;
        if (exception instanceof BusinessException) {
            businessException = (BusinessException) exception;
        } else {
            String throwableClassName = exception.getClass().getName();
            if (throwableClassName.startsWith(CommonConstant.JAVAX_VALIDATION)
                    || throwableClassName.startsWith(CommonConstant.SPRING_VALIDATION)
                    || throwableClassName.startsWith(CommonConstant.SPRING_WEB_BIND)
                    || throwableClassName.startsWith(CommonConstant.SPRING_HTTP_CONVERTER)
                    || throwableClassName.startsWith(CommonConstant.SEARCH_NOT_ALLOW)
                    || throwableClassName.startsWith(CommonConstant.SORT_NOT_ALLOW)) {
                businessException = new BusinessException(ExceptionEnum.PARAMETER_ERROR, exception);
            } else if (throwableClassName.contains(CommonConstant.DAO_OPERATE_EXCEPTION)) {
                businessException = new BusinessException(ExceptionEnum.DAO_OPERATE_ERROR, exception);
            }
        }
        if (Objects.isNull(businessException)) {
            businessException = new BusinessException(ExceptionEnum.SERVER_ERROR, exception);
        }
        logException(businessException);
        response.setStatus(businessException.getStatus());
        SysExceptionJson sysExceptionJson = new SysExceptionJson(businessException);
        RequestLogContextHelper.remove();
        return sysExceptionJson;
    }

    // 日志格式化
    private void logException(BusinessException businessException) {
        String exceptionType = CommonConstant.CUSTOM_EXCEPTION;
        if (businessException.getExceptionEnum() == ExceptionEnum.SERVER_ERROR) {
            exceptionType = CommonConstant.SYSTEM_EXCEPTION;
        }
        String requestParam = RequestUtil.getUri();
        log.error(
                CommonConstant.LINE_SEPARATOR +
                        ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
                        CommonConstant.LINE_SEPARATOR +
                        "异常记录: (" +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) +
                        ") :" +
                        CommonConstant.LINE_SEPARATOR +
                        "@ 请求(" + RequestLogContextHelper.getRequestLog().orElse(new SysRequestLog()).getRequestId() + "): " +
                        requestParam +
                        CommonConstant.LINE_SEPARATOR +
                        "@ 类型: " +
                        exceptionType +
                        CommonConstant.LINE_SEPARATOR +
                        "@ 描述: ",
                businessException
        );
        log.error(
                CommonConstant.LINE_SEPARATOR +
                        "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
}
