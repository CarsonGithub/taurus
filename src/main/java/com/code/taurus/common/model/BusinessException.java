package com.code.taurus.common.model;

import com.code.taurus.common.enums.ExceptionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通用业务异常model
 *
 * @author 郑楷山
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    private int status;

    ExceptionEnum exceptionEnum;

    private Object[] args;

    public BusinessException() {
    }

    /**
     * @param exceptionEnum 异常枚举
     */
    public BusinessException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage(), null, false, false);
        this.exceptionEnum = exceptionEnum;
        this.status = exceptionEnum.getStatus();
    }

    /**
     * @param exceptionEnum 错误枚举
     * @param cause         cause
     */
    public BusinessException(ExceptionEnum exceptionEnum, Throwable cause) {
        super(cause.getMessage(), cause, true, true);
        this.exceptionEnum = exceptionEnum;
        this.status = exceptionEnum.getStatus();
    }

}
