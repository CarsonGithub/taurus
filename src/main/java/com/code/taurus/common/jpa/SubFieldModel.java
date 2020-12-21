package com.code.taurus.common.jpa;

import com.code.taurus.common.entity.AbstractEntity;
import com.code.taurus.common.model.QueryModel;
import com.code.taurus.common.service.impl.AbstractService;
import com.code.taurus.common.util.ReflectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;

/**
 * 帮助类: 集合字段
 *
 * @author 郑楷山
 **/

@Slf4j
@Data
public class SubFieldModel<E extends AbstractEntity> {

    // 当前集合字段名
    private String currentFieldName;
    // 查询上下文
    private QueryContext<E> queryContext;
    // 对象还是集合
    private Boolean isOne;

    public static <E extends AbstractEntity> Optional<SubFieldModel<E>> of(EntityManager em, Field field, Class<E> entityClass, AbstractService<E, ?> service, QueryModel queryModel) {
        Class<?> aClass;
        boolean isOne = ReflectUtil.isOne(field);
        if (isOne) {
            aClass = field.getType();
            queryModel.setIsPaged(true).setPageSize(1).setOffset(1);
        }else {
            Optional<Class<?>> optionalClass = ReflectUtil.getGenericTypeByField(field);
            if (!optionalClass.isPresent()) {
                return Optional.empty();
            } else {
                aClass = optionalClass.get();
            }
            queryModel.setIsPaged(false);
        }
        String fieldName = field.getName();
        queryModel.addIgnoreColumns(fieldName);
        QueryContext<E> queryContext = QueryContext.buildSubField(em, entityClass, service, queryModel, aClass, fieldName);
        if (!queryContext.check()) {
            return Optional.empty();
        }
        SubFieldModel<E> collectionFieldModel = new SubFieldModel<>();
        collectionFieldModel.setIsOne(isOne);
        collectionFieldModel.setQueryContext(queryContext);
        collectionFieldModel.setCurrentFieldName(fieldName);
        return Optional.of(collectionFieldModel);
    }

    @SuppressWarnings("unchecked")
    public <T>T getData(Object conditionValue) {
        AbstractService<E, ?> abstractService = queryContext.getService();
        if (null == abstractService) {
            log.info("集合字段调用findCollection方法时获取不到Bean:{}", queryContext);
            return (T) Collections.emptyList();
        }
        queryContext.buildWhere(conditionValue);
        queryContext.buildTypedQuery();
        Page<?> page = abstractService.findByQueryContext(queryContext);
        if (isOne) {
            return (T) page.getContent().get(0);
        }
        return (T) page.getContent();
    }

}
