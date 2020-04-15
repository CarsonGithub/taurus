package com.taurus.common.jpa;

import com.taurus.common.enums.ConditionEnum;
import com.taurus.common.util.SQLFilterUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 帮助类: SQL条件拼接
 *
 * @author 郑楷山
 **/

public class SpecificationFactory<T>{

    private Specification<T> specification = (root, query, cb) -> null ;

    private Map<String,Specification<T>> conditionMap = new ConcurrentHashMap<>();

    /**
     * And条件的统一入口
     */
    public Specification<T> build() {
        specification = (root, query, cb) -> null ;
        conditionMap.forEach((s, tSpecification) -> specification = specification.and(tSpecification));
        return specification;
    }

    public void append(String name,EnumMap<ConditionEnum,Object> detail) {
        detail.forEach((k,v)-> append(name, k, v));
    }

    public void append(String name, ConditionEnum condition, Object value) {
        if (StringUtils.isBlank(name)
                || (condition != ConditionEnum.NOT_NULL && null == value)
                || (condition != ConditionEnum.NULL && null == value)) {
            return;
        }
        switch (condition) {
            case EQUAL:
                equal(name,value);
                break;
            case UNEQUAL:
                notEqual(name,value);
                break;
            case IN:
                in(name, value);
                break;
            case NOT_IN:
                notIn(name, value);
                break;
            case LIKE_BOTH:
                likeBoth(name, (String) value);
                break;
            case GT:
                gt(name, value);
                break;
            case GT_EQUAL:
                gtEqual(name, value);
                break;
            case LT:
                lt(name, value);
                break;
            case LT_EQUAL:
                ltEqual(name, value);
                break;
            case BETWEEN_EQUAL:
                betweenEqual(name, (Map<String,Object>)value);
                break;
            case NULL:
                isNull(name);
                break;
            case NOT_NULL:
                notNull(name);
                break;
            default:
                break ;
        }
    }

    private void put(String key, Specification<T> spec){
        conditionMap.put(key, spec);
    }


    /**************基础操作***************/

    // like-both
    private void likeBoth(String name, String value) {
        put(name,(root, query, cb) ->
                cb.like(root.get(name), "%"+ SQLFilterUtil.sqlInject(value)+"%"));
    }

    // =
    private void equal(String name, Object value) {
        put(name,(root, query, cb) -> cb.equal(root.get(name), value));
    }

    // !=
    private void notEqual(String name, Object value) {
        put(name,(root, query, cb) -> cb.notEqual(root.get(name), value));
    }

    // >
    private void gt(String name, Object value) {
        put(name,(root, query, cb) -> cb.greaterThan(root.get(name), (Comparable) value));
    }

    // >=
    private void gtEqual(String name, Object value) {
        put(name,(root, query, cb) -> cb.greaterThanOrEqualTo(root.get(name), (Comparable) value));
    }

    // <
    private void lt(String name, Object value) {
        put(name,(root, query, cb) -> cb.lessThan(root.get(name), (Comparable) value));
    }

    // <=
    private void ltEqual(String name, Object value) {
        put(name,(root, query, cb) -> cb.lessThanOrEqualTo(root.get(name), (Comparable) value));
    }

    // betweenEqual
    private void betweenEqual(String name, Map<String,Object> value){
        put(name,(root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (value.get("left") != null ) {
                list.add(cb.greaterThanOrEqualTo(root.get(name), (Comparable)value.get("left")));
            }
            if (value.get("right") != null ) {
                list.add(cb.lessThanOrEqualTo(root.get(name), (Comparable)value.get("right")));
            }
            Predicate[] predicateArray = new Predicate[list.size()];
            query.where(cb.and(list.toArray(predicateArray)));
            return query.getRestriction();
        });
    }

    // is null
    private void isNull(String name) {
        put(name,(root, query, cb) -> cb.isNull(root.get(name)));
    }

    // is not null
    private void notNull(String name) {
        put(name,(root, query, cb) -> cb.isNotNull(root.get(name)));
    }

    // in
    private void in(String name, Object value) {
        put(name,(root, query, cb) -> root.get(name).in(value));
    }

    // not in
    private void notIn(String name, Object value) {
        put(name,(root, query, cb) -> root.get(name).in(value).not());
    }


}