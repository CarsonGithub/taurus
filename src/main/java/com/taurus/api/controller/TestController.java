package com.taurus.api.controller;

import com.taurus.api.entity.Test;
import com.taurus.api.model.TestCreateModel;
import com.taurus.api.model.TestUpdateModel;
import com.taurus.common.controller.AbstractController;
import com.taurus.common.service.IService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器： 测试模块
 *
 * @author 郑楷山
 **/

@Slf4j
@Api(tags = "Backend: 测试")
@RestController
@RequestMapping("/test")
public class TestController extends
        AbstractController<IService<Test, TestCreateModel, TestUpdateModel>
                        , Test, TestCreateModel, TestUpdateModel> {

}
