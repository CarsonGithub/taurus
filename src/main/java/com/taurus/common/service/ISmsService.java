package com.taurus.common.service;

/**
 * 短信处理
 *
 * @author 郑楷山
 **/
public interface ISmsService {

    String getCode(String phoneNumber);

    void checkCode(String phoneNumber, String code);
}
