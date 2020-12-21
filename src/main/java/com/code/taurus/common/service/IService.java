package com.code.taurus.common.service;

import com.code.taurus.common.entity.AbstractEntity;
import com.code.taurus.common.model.AbstractBizVO;
import com.code.taurus.common.model.IdNameVO;
import com.code.taurus.common.model.QueryModel;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * 服务: 基础接口
 *
 * @author 郑楷山
 **/
public interface IService<E extends AbstractEntity, V extends AbstractBizVO> {

    E get(Long id);

    E get(IdNameVO idNameVO);

    V getVO(Long id);

    Page<V> find(QueryModel queryModel);

    E create(E entity);

    E update(E entity);

    void delete(List<IdNameVO> idNameVOS);

    Optional<V> findOneVO(QueryModel queryModel);

    Optional<V> findFirstVO(QueryModel queryModel);

    long count(QueryModel queryModel);

    /**
     * 文件移除(带业务逻辑)
     */
    void getFileRemoveList(E entity, List<String> toRemoveList);

}
