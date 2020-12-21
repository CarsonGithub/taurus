package com.code.taurus.common.jpa;

import com.alibaba.fastjson.JSONObject;
import com.code.taurus.common.entity.AbstractEntity;
import com.code.taurus.common.enums.ExceptionEnum;
import com.code.taurus.common.model.BusinessException;
import com.code.taurus.common.model.QueryModel;
import com.code.taurus.common.service.impl.AbstractService;
import com.code.taurus.common.util.ReflectUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 帮助类: 查询上下文
 *
 * @author 郑楷山
 **/

@Slf4j
public class QueryContext<E extends AbstractEntity> {

    private final EntityManager em;
    private final CriteriaBuilder cb;
    private final Root<E> root;
    @Getter
    private final Class<E> entityClass;
    @Getter
    private final CriteriaQuery<Tuple> query;
    @Getter
    private final AbstractService<E, ?> service;
    @Getter
    private final QueryModel queryModel;

    @Getter
    private final Class<?> voClass;
    @Getter
    private Join<E, Object> join;
    @Getter
    private Specification<E> spec;
    @Getter
    private Pageable pageable;
    @Getter
    private List<String> columns;
    @Getter
    private List<SubFieldModel<E>> childColumns;
    @Getter
    private TypedQuery<Tuple> typedQuery;

    public QueryContext(EntityManager outEm, Class<E> entityClass, Class<?> voClass, AbstractService<E, ?> service, QueryModel queryModel) {
        this.em = outEm;
        this.cb = em.getCriteriaBuilder();
        this.entityClass = entityClass;
        this.voClass = voClass;
        this.service = service;
        this.query = cb.createQuery(Tuple.class);
        this.root = query.from(entityClass);
        this.queryModel = queryModel;
    }

    public boolean check() {
        return hasColumn & hasJoin & hasPageable & hasSelect & service != null;
    }

    public static <E extends AbstractEntity> QueryContext<E> build(
            EntityManager outEm, Class<E> entityClass, AbstractService<E, ?> service, QueryModel queryModel, Class<?> voClass) {
        return new QueryContext<>(outEm, entityClass, voClass, service, queryModel)
                .buildPageable()
                .buildJoin(null)
                .buildColumns(false)
                .buildSelect()
                .buildWhere(null)
                .buildSort()
                .buildTypedQuery();
    }

    public static <E extends AbstractEntity> QueryContext<E> buildSubField(
            EntityManager outEm, Class<E> entityClass, AbstractService<E, ?> service, QueryModel queryModel, Class<?> voClass, String fieldName) {
        return new QueryContext<>(outEm, entityClass, voClass, service, queryModel)
                .buildPageable()
                .buildJoin(fieldName)
                .buildColumns(true)
                .buildSelect()
                .buildSort();
    }


    public QueryContext<E> buildTypedQuery() {
        this.typedQuery = em.createQuery(query);
        return this;
    }

    private boolean hasPageable = false;

    public QueryContext<E> buildPageable() {
        this.pageable = PageableUtil.build(queryModel);
        hasPageable = true;
        return this;
    }

    private boolean hasJoin = false;

    public QueryContext<E> buildJoin(String joinColumn) {
        if (StringUtils.isNotBlank(joinColumn)) {
            this.join = root.join(joinColumn, JoinType.INNER);
        }
        hasJoin = true;
        return this;
    }

    private boolean hasColumn = false;

    public QueryContext<E> buildColumns(boolean isSubFiled) {
        if (!hasJoin) {
            return this;
        }
        if (isSubFiled) {
            List<String> fieldNameList = new ArrayList<>();
            ReflectUtil.getOneOrManyNameList(fieldNameList, this.voClass, true, true);
            queryModel.addIgnoreColumns(fieldNameList);
        }
        this.childColumns = new ArrayList<>();
        this.columns = new ArrayList<>();

        this.buildFieldList(this.voClass, new QueryModel(), this.columns, childColumns);
        hasColumn = !CollectionUtils.isEmpty(this.columns);
        this.columns = this.columns.stream().distinct().collect(Collectors.toList());
        this.columns.remove("fdId");
        this.columns.add(0, "fdId");
        return this;
    }

    private boolean hasSelect = false;

    public QueryContext<E> buildSelect() {
        if (!hasColumn) {
            return this;
        }
        if (CollectionUtils.isEmpty(columns)) {
            return this;
        }
        List<Selection<?>> paths = new ArrayList<>();
        if (null == join) {
            columns.forEach(field -> paths.add(root.get(field)));
        } else {
            columns.forEach(field -> paths.add(join.get(field)));
        }
        hasSelect = !CollectionUtils.isEmpty(paths);
        if (hasSelect) {
            query.multiselect(paths);
        }
        return this;
    }

    public QueryContext<E> buildWhere(Object relId) {
        List<Predicate> predicateList = new ArrayList<>();
        this.getSpecFromQM(queryModel);
        if (null != relId) {
            Predicate id = cb.equal(root.get("fdId"), relId);
            predicateList.add(id);
        }
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, cb);
            if (null != predicate) {
                predicateList.add(predicate);
            }
        }
        if (!CollectionUtils.isEmpty(predicateList)) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            query.where(cb.and(predicateList.toArray(predicates)));
        }
        return this;
    }

    public QueryContext<E> buildSort() {
        query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        return this;
    }

    /**
     * 查询模型转条件参数
     */
    private void getSpecFromQM(QueryModel queryModel) {
        SpecModel<E> specModel = new SpecModel<>(entityClass);
        Map<String, Object> conditions = queryModel.getConditions();
        if (!CollectionUtils.isEmpty(conditions)) {
            conditions.forEach(specModel::append);
        }
        this.spec = specModel.build();
    }

    /**
     * 获取所有字段名
     */
    private void buildFieldList(Class<?> clazz, QueryModel childQueryModel, List<String> fieldNameList, List<SubFieldModel<E>> collectionFields) {
        if (clazz == null) {
            return;
        }
        List<String> columns = queryModel.getColumns();
        List<String> ignoreColumns = queryModel.getIgnoreColumns();
        Arrays.asList(clazz.getDeclaredFields()).forEach(field -> {
            String fieldName = field.getName();
            if ("serialVersionUID".equalsIgnoreCase(fieldName)) {
                return;
            }
            if (!CollectionUtils.isEmpty(columns) && !(columns.contains(fieldName))) {
                return;
            }
            if (!CollectionUtils.isEmpty(ignoreColumns) && ignoreColumns.contains(fieldName)) {
                return;
            }
            field.setAccessible(true);
            if (ReflectUtil.isMany(field) || ReflectUtil.isOne(field)) {
                /*if (null == collectionFields) {
                    return;
                }*/
                Optional<SubFieldModel<E>> optional = SubFieldModel.of(em, field, entityClass, service, childQueryModel);
                if (!optional.isPresent()) {
                    log.info("生成集合字段模型出错:" + fieldName);
                } else {
                    collectionFields.add(optional.get());
                }
            } else {
                fieldNameList.add(fieldName);
            }
        });
        buildFieldList(clazz.getSuperclass(), childQueryModel, fieldNameList, collectionFields);
    }

    /**
     * 初始化单个VO对应的json对象
     **/
    public JSONObject getJSONObject() {
        if (CollectionUtils.isEmpty(columns)) {
            throw new BusinessException(ExceptionEnum.LIST_DATA_ERROR);
        }
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < columns.size(); i++) {
            jsonObject.put(new ArrayList<>(columns).get(i), i);
        }
        return jsonObject;
    }

}
