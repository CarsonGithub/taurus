package com.taurus.api.service.impl;

import com.taurus.api.entity.BizUser;
import com.taurus.api.model.BizUserVO;
import com.taurus.api.service.IBizUserService;
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
public class BizUserService extends AbstractService<BizUser, BizUserVO> implements IBizUserService {

	public BizUserService() {
		super(BizUser.class);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public BizUser getByName(String name) {
		SpecificationFactory<BizUser> factory = new SpecificationFactory<>();
		factory.append(CommonConstant.SEARCH_NAME, ConditionEnum.EQUAL,name);
		BizUser bizUser = iRepository.findOne(factory.build()).orElse(null);
		if (null == bizUser) {
			throw new BusinessException(ExceptionEnum.NO_EXIST_ERROR);
		}
		return bizUser;
	}

	@Override
	@Transactional
	public BizUser create(BizUserVO bizUserVO) {
		bizUserVO.setPassword(passwordEncoder.encode(bizUserVO.getPassword()));
		return super.create(bizUserVO);
	}

	@Override
	@Transactional
	public BizUser reset(BizUser bizUser, String password) {
		bizUser.setFdPassword(passwordEncoder.encode(password));
		return iRepository.save(bizUser);
	}
}
