package com.code.taurus.common.config.security;

import com.code.taurus.common.model.SysSecurityUser;

import java.util.Optional;

/**
 * 当前线程用户存储器
 *
 * @author 郑楷山
 **/

public class UserContextHelper {

    private static final ThreadLocal<SysSecurityUser> userThreadLocal = new ThreadLocal<>();

    public static Optional<SysSecurityUser> getUser() {
        SysSecurityUser sysSecurityUser = userThreadLocal.get();
        return Optional.ofNullable(sysSecurityUser);
    }

    public static boolean isPresent() {
        return getUser().isPresent();
    }

    public static void setUser(SysSecurityUser user) {
        userThreadLocal.set(user);
    }

    public static void remove() {
        userThreadLocal.remove();
    }

}
