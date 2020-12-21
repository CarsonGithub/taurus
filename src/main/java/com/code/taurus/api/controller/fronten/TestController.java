package com.code.taurus.api.controller.fronten;

import com.code.taurus.api.entity.BizTest;
import com.code.taurus.api.model.BizTestVO;
import com.code.taurus.common.controller.AbstractController;
import com.code.taurus.common.service.IService;
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
@Api(tags = "Frontend: 测试")
@RestController
@RequestMapping("/api/test")
public class TestController extends AbstractController<IService<BizTest, BizTestVO>, BizTest, BizTestVO> {

}
