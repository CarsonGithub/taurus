package com.taurus.api.service.impl;

import com.taurus.api.entity.Test;
import com.taurus.api.model.TestCreateModel;
import com.taurus.api.model.TestUpdateModel;
import com.taurus.api.service.ITestService;
import com.taurus.common.service.impl.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class TestService extends AbstractService<Test, TestCreateModel, TestUpdateModel> implements ITestService {

	public TestService() {
		super(Test.class);
	}

}
