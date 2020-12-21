package com.code.taurus.common.jpa;

import com.code.taurus.common.entity.AbstractEntity;
import com.code.taurus.common.enums.ConditionEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 帮助类: SQL条件接
 *
 * @author 郑楷山
 **/

public class SpecModel<E extends AbstractEntity> {

    private final Class<E> entityClass;

    private final Map<String, Specification<E>> conditionMap;

    private Specification<E> specification;

    public SpecModel(Class<E> entityClass) {
        this.entityClass = entityClass;
        this.conditionMap = new LinkedHashMap<>();
        this.specification = (root, query, cb) -> null;
    }

    public Specification<E> build() {
        return build(true);
    }

    public Specification<E> build(boolean isAnd) {
        if (isAnd) {
            conditionMap.forEach((k, spec) -> specification = specification.and(spec));
        } else {
            conditionMap.forEach((k, spec) -> specification = specification.or(spec));
        }
        return specification;
    }

    @SuppressWarnings("unchecked")
    public SpecModel<E> append(String name, Object detail) {
        Map<String, Object> detailMap = new LinkedHashMap<>();
        if (detail instanceof Map) {
            detailMap = (Map<String, Object>) detail;
        } else {
            detailMap.put(ConditionEnum.EQUAL.getValue(), detail);
        }
        detailMap.forEach((k, v) -> append(name, k, v));
        return this;
    }

    public SpecModel<E> append(String name, ConditionEnum condition, Object value) {
        return this.append(name, condition.getValue(), value);
    }

    public SpecModel<E> append(String name, String condition, Object value) {
        Specification<E> specification = SpecUtil.getSpec(entityClass, name, condition, value);
        if (null != specification) {
            this.conditionMap.put(name, specification);
        }
        return this;
    }

}
