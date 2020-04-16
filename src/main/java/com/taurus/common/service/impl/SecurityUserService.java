package com.taurus.common.service.impl;

import com.taurus.api.entity.BizUser;
import com.taurus.api.service.IBizUserService;
import com.taurus.common.model.SysSecurityUser;
import com.taurus.common.service.ISecurityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityUserService implements ISecurityUserService {

	@Autowired
	private IBizUserService ISysUserService;

	@Override
    public SysSecurityUser getByUserName(String name) throws UsernameNotFoundException {
        BizUser bizUser = ISysUserService.getByName(name);
        if (bizUser == null || null == bizUser.getFdRole()) {
            throw new UsernameNotFoundException(String.format("用户: %s 不存在!", name));
        }
        SysSecurityUser securityUserVO = new SysSecurityUser();
        BeanUtils.copyProperties(bizUser, securityUserVO);
        return securityUserVO;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getByUserName(s);
    }
}
