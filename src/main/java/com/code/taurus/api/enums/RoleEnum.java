package com.code.taurus.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

/**
 * 枚举:角色
 *
 * @author 郑楷山
 **/

@Getter
@AllArgsConstructor
public enum RoleEnum implements GrantedAuthority {

	ADMIN(0),

	VENDOR(1),

	USER(2),

	;

	private final int value;

	@Override
	public String getAuthority() {
		return this.name();
	}
}
