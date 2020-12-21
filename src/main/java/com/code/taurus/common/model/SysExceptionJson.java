package com.code.taurus.common.model;

import com.alibaba.fastjson.JSONObject;
import com.code.taurus.common.config.security.RequestLogContextHelper;
import com.code.taurus.common.constant.CommonConstant;
import com.code.taurus.common.enums.ExceptionEnum;

/**
 * 异常返回model（Json）
 *
 * @author 郑楷山
 **/

public class SysExceptionJson extends JSONObject {

    public SysExceptionJson(ExceptionEnum exceptionEnum) {
        this.put(CommonConstant.STATUS_CODE, exceptionEnum.getStatus());
        this.put(CommonConstant.TIMESTAMP, System.currentTimeMillis());
        this.put(CommonConstant.MESSAGE, exceptionEnum.getMessage());
    }

    public SysExceptionJson(Exception exception) {
        if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException) exception;
            this.put(CommonConstant.STATUS_CODE, businessException.getStatus());
            this.put(CommonConstant.TIMESTAMP, System.currentTimeMillis());
            this.put(CommonConstant.MESSAGE, businessException.getExceptionEnum().getMessage());
        }
        this.put("requestId", RequestLogContextHelper.getRequestLog().orElse(new SysRequestLog()).getRequestId());
        this.put("description", exception.getMessage());
    }

}
