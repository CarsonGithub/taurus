package com.taurus.common.config;

import com.alibaba.fastjson.JSONObject;
import com.taurus.common.constant.CommonConstant;
import com.taurus.common.enums.ExceptionEnum;
import com.taurus.common.model.BusinessException;
import com.taurus.common.model.SysExceptionJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static com.taurus.common.constant.CommonConstant.LINE_SEPARATOR;

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

        BusinessException businessException;
        String throwableClassName = exception.getClass().getName();
        if (throwableClassName.startsWith(CommonConstant.JAVAX_VALIDATION)
                || throwableClassName.startsWith(CommonConstant.SPRING_VALIDATION)
                || throwableClassName.startsWith(CommonConstant.SPRING_WEB_BIND)
                || throwableClassName.startsWith(CommonConstant.SPRING_HTTP_CONVERTER)
                || throwableClassName.startsWith(CommonConstant.SEARCH_NOT_ALLOW)
                || throwableClassName.startsWith(CommonConstant.SORT_NOT_ALLOW)) {
            exception = new BusinessException(ExceptionEnum.PARAMETER_ERROR, exception.getMessage());
        }else if(throwableClassName.contains(CommonConstant.DAO_OPERATE_EXCEPTION)){
            exception = new BusinessException(ExceptionEnum.DAO_OPERATE_ERROR, exception.getMessage());
        }
        if (exception instanceof BusinessException) {
            businessException = (BusinessException) exception;
            logException(businessException, exception,CommonConstant.BUSINESS_EXCEPTION);
        } else {
            businessException = new BusinessException(ExceptionEnum.SERVER_ERROR, exception.getMessage());
            logException(businessException, exception,CommonConstant.SYSTEM_EXCEPTION);
        }
        response.setStatus(businessException.getStatus());
        return new SysExceptionJson(businessException);
    }

    // 日志格式化
    private void logException(BusinessException businessException, Exception ex, String exceptionType) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(byteArrayOutputStream, true)) {
            ex.printStackTrace(printStream);
            String stackTrace = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
            log.error(
                    new StringBuffer()
                            .append(LINE_SEPARATOR)
                            .append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                            .append(LINE_SEPARATOR)
                            .append("异常记录: (")
                            .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                            .append(") :")
                            .append(LINE_SEPARATOR)
                            .append("@ 类型: ")
                            .append(exceptionType)
                            .append(LINE_SEPARATOR)
                            .append("@ 代码: ")
                            .append(businessException.getError())
                            .append(LINE_SEPARATOR)
                            .append("@ 描述: ")
                            .append(stackTrace)
                            .append(LINE_SEPARATOR)
                            .append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
                            .toString()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
