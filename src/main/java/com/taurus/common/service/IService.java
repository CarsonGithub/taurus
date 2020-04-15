package com.taurus.common.service;

import com.taurus.common.entity.AbstractEntity;
import com.taurus.common.model.AbstractModel;
import com.taurus.common.model.QueryModel;
import com.taurus.common.model.AbstractUpdateModel;
import org.springframework.data.domain.Page;

/**
 * 服务: 基础接口
 *
 * @author 郑楷山
 **/
public interface IService<T extends AbstractEntity,C extends AbstractModel,U extends AbstractUpdateModel> {

    T getById(Long id);

    Page<T> list(QueryModel queryModel);

    T create(C createModel);

    T update(U updateModel);

    void delete(Long id);

    void deleteAll(String ids);

}
