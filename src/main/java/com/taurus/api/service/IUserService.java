package com.taurus.api.service;

import com.taurus.api.entity.User;
import com.taurus.api.model.UserCreateModel;
import com.taurus.api.model.UserUpdateModel;
import com.taurus.common.service.IService;

/**
 * 服务： 用户
 *
 * @author 郑楷山
 **/

public interface IUserService extends IService<User,UserCreateModel,UserUpdateModel> {

	User getByName(String name);

	User reset(User user, String password);

}
