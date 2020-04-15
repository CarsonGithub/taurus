package com.taurus.common.config.security;

import com.taurus.common.model.SysSecurityUser;

/**
 * 当前线程用户存储器
 *
 * @author 郑楷山
 **/

public class UserContextHelper {

	private static final ThreadLocal<SysSecurityUser> userThreadLocal = new ThreadLocal<>();

	public static SysSecurityUser getUser() {
		return userThreadLocal.get();
	}

	public static SysSecurityUser setUser(SysSecurityUser user) {
		userThreadLocal.set(user);
		return user;
	}

	public static void remove() {
		userThreadLocal.remove();
	}

}
