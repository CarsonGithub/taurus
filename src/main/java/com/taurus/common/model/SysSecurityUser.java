package com.taurus.common.model;

import com.taurus.api.entity.BizUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

/**
 * security验证用户model
 *
 * @author 郑楷山
 **/

@Data
@EqualsAndHashCode(callSuper = false)
public class SysSecurityUser extends BizUser implements UserDetails {

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(this.getFdRole());
	}

	@Override
	public String getPassword() {
		return this.getFdPassword();
	}

	@Override
	public String getUsername() {
		return this.getFdName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
