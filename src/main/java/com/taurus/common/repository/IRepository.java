package com.taurus.common.repository;

import com.google.common.collect.Lists;
import com.taurus.common.entity.AbstractEntity;
import com.taurus.common.enums.ConditionEnum;
import com.taurus.common.jpa.PageableUtil;
import com.taurus.common.jpa.SpecificationFactory;
import com.taurus.common.model.QueryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 持久: 基础接口
 *
 * @author 郑楷山
 **/
public interface IRepository<T extends AbstractEntity> extends JpaRepositoryImplementation<T, Long> {

    default Page<T> list(QueryModel queryModel, Class<T> clazz, EntityManager em) {
        Pageable pageable = PageableUtil.build(queryModel);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clazz);
        Root<T> root = query.from(clazz);
        Specification<T> spec = getSpec(queryModel);
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, cb);
            if (predicate != null) {
                query.where(predicate);
            }
        }
        query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        // 分页
        if (queryModel.isPaged()) {
            List<T> resultList = em.createQuery(query)
                    .setMaxResults(queryModel.getPageSize())
                    .setFirstResult((queryModel.getOffset() - 1) * queryModel.getPageSize())
                    .getResultList();
            return new PageImpl<>(resultList, pageable, count(spec));
        }
        // 不分页
        List<Selection<?>> paths = Lists.newArrayList();
        Arrays.stream(getSearchField(clazz)).forEach(field -> paths.add(root.get(field)));
        if (paths.size() > 0) {
            query.multiselect(paths);
        }
        return new PageImpl<>(em.createQuery(query).getResultList());
    }

    default List<Field> getFieldList(List<Field> fieldList, Class clazz) {
        if (clazz == null) {
            return fieldList;
        }
        Arrays.asList(clazz.getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            fieldList.add(field);
        });
        return getFieldList(fieldList, clazz.getSuperclass());
    }

    default String[] getSearchField(Class clazz) {
        Constructor[] cons = clazz.getDeclaredConstructors();
        int len;
        String[] searchField = new String[0];
        for (Constructor con : cons) {
            Parameter[] parameters = con.getParameters();
            len = parameters.length;
            if (len > 0) {
                searchField = new String[len];
                for (int i = 0; i < len; i++) {
                    searchField[i] = parameters[i].getName();
                }
            }
        }
        return searchField;
    }

    default Specification<T> getSpec(QueryModel queryModel) {
        SpecificationFactory<T> factory = new SpecificationFactory<>();
        Map<String, EnumMap<ConditionEnum, Object>> conditions = queryModel.getConditions();
        if (conditions != null && !conditions.isEmpty()) {
            conditions.forEach(factory::append);
        }
        return factory.build();
    }

}
