package com.code.taurus.common.service.impl;

import com.code.taurus.api.model.BizUserVO;
import com.code.taurus.api.service.BizUserService;
import com.code.taurus.common.model.SysSecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private BizUserService bizUserService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        BizUserVO userVO = bizUserService.getByName(s);
        if (userVO == null || null == userVO.getFdRole()) {
            throw new UsernameNotFoundException(String.format("用户: %s 不存在!", s));
        }
        SysSecurityUser securityUser = new SysSecurityUser();
        BeanUtils.copyProperties(userVO, securityUser);
        return securityUser;
    }
}
