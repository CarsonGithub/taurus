package com.code.taurus.common.controller;

import com.code.taurus.common.entity.AbstractEntity;
import com.code.taurus.common.model.QueryModel;
import com.code.taurus.common.service.IService;
import com.code.taurus.common.model.AbstractBizVO;
import com.code.taurus.common.model.IdNameVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * 通用: 控制器
 *
 * @author 郑楷山
 **/

@Slf4j
@Api
public abstract class AbstractController<S extends IService<E, V>, E extends AbstractEntity, V extends AbstractBizVO> {

    @Autowired
    private S iService;

    public S getApi() {
        return iService;
    }

    @ApiOperation("获取数据")
    @PostMapping("/find")
    public Page<V> find(@RequestBody QueryModel queryModel) {
        return iService.find(queryModel);
    }

    @ApiOperation("添加单记录")
    @PostMapping("/add")
    public Long create(@RequestBody @Valid E t) {
        return iService.create(t).getFdId();
    }

    @ApiOperation("更新单记录")
    @PostMapping("/update")
    public void update(@RequestBody @Valid E t) {
        iService.update(t);
    }

    @ApiOperation("删除记录,可多删除")
    @PostMapping("/delete")
    public void delete(@RequestBody List<IdNameVO> idNameVOList) {
        iService.delete(idNameVOList);
    }
}

