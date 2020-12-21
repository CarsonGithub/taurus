package com.code.taurus.common.config.security;

import com.code.taurus.common.model.SysRequestLog;

import java.util.Optional;

/**
 * 当前线程请求记录存储器
 *
 * @author 郑楷山
 **/

public class RequestLogContextHelper {

    private static final ThreadLocal<SysRequestLog> requestLogThreadLocal = new ThreadLocal<>();

    public static Optional<SysRequestLog> getRequestLog() {
        SysRequestLog sysRequestLog = requestLogThreadLocal.get();
        return Optional.ofNullable(sysRequestLog);
    }

    public static boolean isPresent() {
        return getRequestLog().isPresent();
    }

    public static void setRequestLog(SysRequestLog requestLog) {
        requestLogThreadLocal.set(requestLog);
    }

    public static void remove() {
        requestLogThreadLocal.remove();
    }

}
