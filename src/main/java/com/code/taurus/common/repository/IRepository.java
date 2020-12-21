package com.code.taurus.common.repository;

import com.code.taurus.common.entity.AbstractEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

/**
 * 持久: 基础接口
 *
 * @author 郑楷山
 **/
public interface IRepository<E extends AbstractEntity> extends JpaRepositoryImplementation<E, Long> {
}
