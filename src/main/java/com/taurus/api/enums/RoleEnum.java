package com.taurus.api.enums;

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

	SUPER_ADMIN(0),

	USER(1),

	;

	private final int value;

	@Override
	public String getAuthority() {
		return this.name();
	}
}
