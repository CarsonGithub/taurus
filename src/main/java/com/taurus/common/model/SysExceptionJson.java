package com.taurus.common.model;

import com.alibaba.fastjson.JSONObject;
import com.taurus.common.constant.CommonConstant;
import com.taurus.common.enums.ExceptionEnum;

/**
 * 异常返回model（Json）
 *
 * @author 郑楷山
 **/

public class SysExceptionJson extends JSONObject {

    public SysExceptionJson(ExceptionEnum exceptionEnum) {
        this.put(CommonConstant.STATUS_CODE,exceptionEnum.getStatus()) ;
        this.put(CommonConstant.ERROR_CODE,exceptionEnum.getError()) ;
        this.put(CommonConstant.TIMESTAMP,System.currentTimeMillis()) ;
        this.put(CommonConstant.MESSAGE,exceptionEnum.getDescription()) ;
    }

    public SysExceptionJson(BusinessException businessException) {
        this.put(CommonConstant.STATUS_CODE,businessException.getStatus()) ;
        this.put(CommonConstant.ERROR_CODE,businessException.getError()) ;
        this.put(CommonConstant.TIMESTAMP,System.currentTimeMillis()) ;
        this.put(CommonConstant.MESSAGE,businessException.getMessage()) ;
    }

}
