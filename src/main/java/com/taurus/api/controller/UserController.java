package com.taurus.api.controller;

import com.taurus.api.entity.BizUser;
import com.taurus.api.model.BizUserVO;
import com.taurus.api.service.impl.BizUserService;
import com.taurus.common.controller.AbstractController;
import com.taurus.common.enums.ExceptionEnum;
import com.taurus.common.model.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 控制器： 用户
 *
 * @author 郑楷山
 **/

@Slf4j
@Api(tags = "Backend: 用户")
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController<BizUserService, BizUser, BizUserVO> {

	@ApiOperation("重置密码")
	@PutMapping("/reset/{id}/{password}")
	public void reset(@PathVariable  @ApiParam(value = "id", example = "1" ,required = true) Long id,
					  @PathVariable @ApiParam(value = "新密码", example = "123456" ,required = true) String password){
		BizUser bizUser = getApi().getById(id);
		if(bizUser == null){
			throw new BusinessException(ExceptionEnum.NO_EXIST_ERROR);
		}
		getApi().reset(bizUser,password);
	}

}

