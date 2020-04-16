package com.taurus.api.service;

import com.taurus.api.entity.BizUser;
import com.taurus.api.model.BizUserVO;
import com.taurus.common.service.IService;

/**
 * 服务： 用户
 *
 * @author 郑楷山
 **/

public interface IBizUserService extends IService<BizUser, BizUserVO> {

	BizUser getByName(String name);

	BizUser reset(BizUser bizUser, String password);

}
