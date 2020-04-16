package com.taurus.common.service;

import com.taurus.common.entity.AbstractEntity;
import com.taurus.common.model.AbstractVO;
import com.taurus.common.model.QueryModel;
import org.springframework.data.domain.Page;

/**
 * 服务: 基础接口
 *
 * @author 郑楷山
 **/
public interface IService<T extends AbstractEntity,V extends AbstractVO> {

    T getById(Long id);

    Page<T> list(QueryModel queryModel);

    T create(V vo);

    T update(V vo);

    void delete(Long id);

    void deleteAll(String ids);

}
