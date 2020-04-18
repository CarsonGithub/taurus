package com.taurus.common.controller;

import com.taurus.common.entity.AbstractEntity;
import com.taurus.common.model.AbstractVO;
import com.taurus.common.model.QueryModel;
import com.taurus.common.service.IService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 通用: 控制器
 *
 * @author 郑楷山
 **/

@Slf4j
@Api
public abstract class AbstractController<S extends IService<T, V>, T extends AbstractEntity, V extends AbstractVO> {

	@Autowired
	private S baseService;

	public S getApi() {
		return baseService;
	}

	@ApiOperation("获取单记录")
	@GetMapping("/{id}")
	public T getById(@PathVariable @ApiParam(value = "id", example = "1", required = true) Long id) {
		return baseService.getById(id);
	}

	@ApiOperation("获取列表数据")
	@GetMapping
	public Page<T> list(@Valid QueryModel queryModel) {
		return baseService.list(queryModel);
	}

	@ApiOperation("添加单记录,不要传ID")
	@PostMapping
	public void create(@RequestBody @Valid V vo) {
		baseService.create(vo);
	}

	@ApiOperation("更新单记录,需要传ID")
	@PutMapping
	public void update(@RequestBody @Valid V vo) {
		baseService.update(vo);
	}

	@ApiOperation("删除记录,可多删除")
	@DeleteMapping("/{ids}")
	public void delete(@PathVariable @ApiParam(value = "id", example = "1,2", required = true) String ids) {
		baseService.deleteAll(ids);
	}

}

