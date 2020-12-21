package com.code.taurus.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * api请求日志输出model
 *
 * @author 郑楷山
 **/

@Accessors(chain = true)
@Data
public class SysRequestLog {

    /**
     * 请求对应的IP地址
     */
    private String requestIPAddress;

    /**
     * 处理请求的
     */
    private String processServiceName;

    /**
     * 请求开始时间
     */
    private long requestTime;

    /**
     * 调用的方法名称
     */
    private String methodName;

    /**
     * 请求的uri
     */
    private String requestUri;

    /**
     * 参数
     */
    private Object[] parameter;

    /**
     * 请求的UUID
     */
    private String requestId;


}
