package com.code.taurus.api.service;

import com.code.taurus.api.entity.BizTest;
import com.code.taurus.api.model.BizTestVO;
import com.code.taurus.common.service.IService;
import com.code.taurus.common.service.impl.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class BizTestService extends AbstractService<BizTest, BizTestVO> implements IService<BizTest, BizTestVO> {

    public BizTestService() {
        super(BizTest.class);
    }

}
