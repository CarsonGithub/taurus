package com.taurus.api.service;

import com.taurus.api.entity.Test;
import com.taurus.api.model.TestCreateModel;
import com.taurus.api.model.TestUpdateModel;
import com.taurus.common.service.IService;

/**
 * 服务： 测试
 *
 * @author 郑楷山
 **/

public interface ITestService extends IService<Test, TestCreateModel, TestUpdateModel> {


}
