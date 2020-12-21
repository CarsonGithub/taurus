package com.code.taurus.common.config.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

/**
 * token过期
 *
 * @author 郑楷山
 **/

@Getter
@EqualsAndHashCode(callSuper = false)
public class UPAuTokenWithExpiration extends UsernamePasswordAuthenticationToken {

	private final Date expiration;

	public UPAuTokenWithExpiration(Object principal, Object credentials,Collection<? extends GrantedAuthority> authorities, Date expiration) {
		super(principal, credentials, authorities);
		this.expiration = expiration;
	}
}
