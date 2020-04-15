package com.taurus.common.service;

import com.taurus.common.model.SysSecurityUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * security获取验证用户服务
 *
 * @author 郑楷山
 **/

public interface ISecurityUserService extends UserDetailsService {

    SysSecurityUser getByUserName(String name);

}
