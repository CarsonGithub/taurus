package com.taurus.common.service.impl;

import com.taurus.api.entity.User;
import com.taurus.api.service.IUserService;
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
	private IUserService IUserService;

	@Override
    public SysSecurityUser getByUserName(String name) throws UsernameNotFoundException {
        User user = IUserService.getByName(name);
        if (user == null || null == user.getFdRole()) {
            throw new UsernameNotFoundException(String.format("用户: %s 不存在!", name));
        }
        SysSecurityUser securityUserModel = new SysSecurityUser();
        BeanUtils.copyProperties(user, securityUserModel);
        return securityUserModel;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getByUserName(s);
    }
}
