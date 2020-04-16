package com.taurus.api.service.impl;

import com.taurus.api.entity.BizTest;
import com.taurus.api.model.BizTestVO;
import com.taurus.api.service.IBizTestService;
import com.taurus.common.service.impl.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class BizTestService extends AbstractService<BizTest, BizTestVO> implements IBizTestService {

	public BizTestService() {
		super(BizTest.class);
	}

}
