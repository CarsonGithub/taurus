package com.taurus.api.service.impl;

import com.taurus.api.entity.User;
import com.taurus.api.model.UserCreateModel;
import com.taurus.api.model.UserUpdateModel;
import com.taurus.api.service.IUserService;
import com.taurus.common.constant.CommonConstant;
import com.taurus.common.enums.ConditionEnum;
import com.taurus.common.enums.ExceptionEnum;
import com.taurus.common.jpa.SpecificationFactory;
import com.taurus.common.model.BusinessException;
import com.taurus.common.service.impl.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends AbstractService<User,UserCreateModel,UserUpdateModel> implements IUserService {

	public UserService() {
		super(User.class);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User getByName(String name) {
		SpecificationFactory<User> factory = new SpecificationFactory<>();
		factory.append(CommonConstant.SEARCH_NAME, ConditionEnum.EQUAL,name);
		User user = iRepository.findOne(factory.build()).orElse(null);
		if (null == user) {
			throw new BusinessException(ExceptionEnum.NO_EXIST_ERROR);
		}
		return user;
	}

	@Override
	@Transactional
	public User create(UserCreateModel userCreateModel) {
		userCreateModel.setPassword(passwordEncoder.encode(userCreateModel.getPassword()));
		return super.create(userCreateModel);
	}

	@Override
	@Transactional
	public User reset(User user, String password) {
		user.setFdPassword(passwordEncoder.encode(password));
		return iRepository.save(user);
	}
}
