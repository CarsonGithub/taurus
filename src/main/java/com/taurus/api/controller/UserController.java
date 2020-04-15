package com.taurus.api.controller;

import com.taurus.api.entity.User;
import com.taurus.api.model.UserCreateModel;
import com.taurus.api.model.UserUpdateModel;
import com.taurus.api.service.IUserService;
import com.taurus.common.enums.ExceptionEnum;
import com.taurus.common.model.BusinessException;
import com.taurus.common.model.QueryModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 控制器： 用户
 *
 * @author 郑楷山
 **/

@Slf4j
@Api(tags = "Backend: 用户")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService IUserService;

	@ApiOperation("单记录")
	@GetMapping("/{id}")
	public User getById(@PathVariable @ApiParam(value = "id", example = "1" ,required = true) Long id) {
		return IUserService.getById(id);
	}

	@ApiOperation("列表")
	@GetMapping
	public Page<User> list(@Valid QueryModel userQueryModel) {
		return IUserService.list(userQueryModel);
	}

	@ApiOperation("添加")
	@PostMapping
	public void create(@Valid UserCreateModel userCreateModel) {
		IUserService.create(userCreateModel);
	}

	@ApiOperation("更新")
	@PutMapping
	public void update(@Valid  UserUpdateModel userUpdateModel) {
		IUserService.update(userUpdateModel) ;
	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable  @ApiParam(value = "id", example = "1" ,required = true) Long id){
		IUserService.delete(id);
	}

	@ApiOperation("重置密码")
	@PutMapping("/reset/{id}/{password}")
	public void reset(@PathVariable  @ApiParam(value = "id", example = "1" ,required = true) Long id,
					  @PathVariable @ApiParam(value = "新密码", example = "123456" ,required = true) String password){
		User user = IUserService.getById(id);
		if(user == null){
			throw new BusinessException(ExceptionEnum.NO_EXIST_ERROR);
		}
		IUserService.reset(user,password);
	}

}

