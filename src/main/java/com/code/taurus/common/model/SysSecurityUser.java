package com.code.taurus.common.model;

import com.code.taurus.api.model.BizUserVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * security验证用户model
 *
 * @author 郑楷山
 **/

public class SysSecurityUser extends BizUserVO implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(getFdRole());
    }

    @Override
    public String getPassword() {
        return getFdPassword();
    }

    @Override
    public String getUsername() {
        return getFdName();
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
