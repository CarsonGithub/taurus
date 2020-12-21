package com.code.taurus.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.taurus.common.config.security.UserContextHelper;
import com.code.taurus.common.entity.AbstractEntity;
import com.code.taurus.common.jpa.QueryContext;
import com.code.taurus.common.jpa.SubFieldModel;
import com.code.taurus.common.model.*;
import com.code.taurus.common.repository.IRepository;
import com.code.taurus.common.service.IService;
import com.code.taurus.common.util.BeanCopyUtil;
import com.code.taurus.common.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 服务: 基础类实现
 *
 * @author 郑楷山
 **/
@Slf4j
@SuppressWarnings("unchecked")
public abstract class AbstractService<E extends AbstractEntity, V extends AbstractBizVO> implements IService<E, V> {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    protected IRepository<E> iRepository;
    @Autowired
    private FileService iFileService;

    private final Class<E> entityClass;
    private Class<V> voClass;

    public AbstractService(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public AbstractService(Class<E> entityClass, Class<V> voClass) {
        this.entityClass = entityClass;
        this.voClass = voClass;
    }

    protected void beforeCreateOrUpdate(E entity, boolean isCreate) {
        if (isCreate) {
            entity.beforeCreate();
        }
        Optional<SysSecurityUser> optional = UserContextHelper.getUser();
        entity.setFdUpdateBy(optional.isPresent() ? optional.get().getUsername() : "");
        entity.setFdUpdateTime(new Date());
    }

    @Override
    public E get(Long id) {
        return iRepository.findById(id).orElseThrow(NoRecordException::new);
    }

    @Override
    public E get(IdNameVO idNameVO) {
        return iRepository.findById(idNameVO.getFdId()).orElseThrow(NoRecordException::new);
    }

    @Override
    public V getVO(Long id) {
        return findOneVO(new QueryModel().addCondition("fdId", id)).orElseThrow(NoRecordException::new);

    }

    @Override
    public Optional<V> findOneVO(QueryModel queryModel) {
        Page<V> page = this.find(queryModel.setIsPaged(true).setPageSize(1).setOffset(1));
        if (page.getTotalElements() == 1) {
            return Optional.of(page.getContent().get(0));
        }
        return Optional.empty();
    }

    @Override
    public Optional<V> findFirstVO(QueryModel queryModel) {
        Page<V> page = this.find(queryModel.setIsPaged(true).setPageSize(1).setOffset(1));
        if (page.getTotalElements() > 0) {
            return Optional.of(page.getContent().get(0));
        }
        return Optional.empty();
    }

   /* @Override
    public Optional<E> findEntity(QueryModel queryModel) {
        Page<V> page = this.list(queryModel.setIsPaged(true).setPageSize(1).setOffset(1));
        if (page.getTotalElements() == 1) {
            return Optional.of(get(page.getContent().get(0).getFdId()));
        }
        return Optional.empty();
    }*/

    @Override
    @Transactional
    public void delete(List<IdNameVO> idNameVOS) {
        idNameVOS.forEach(idNameVO -> {
            E entity = this.get(idNameVO);
            // 删除暂时去掉自动移除文件的功能,避免误删导致找不回图片
//            List<String> toRemoveList = new ArrayList<>();
//            getFileRemoveList(entity, toRemoveList);
            iRepository.delete(entity);
//            doFileRemove(toRemoveList);
        });
    }

    @Override
    @Transactional
    public E create(E entity) {
        beforeCreateOrUpdate(entity, true);
        E saveEntity = iRepository.save(entity);
        try {
            Field fieldOrder = saveEntity.getClass().getDeclaredField("orders");
            fieldOrder.setAccessible(true);
            fieldOrder.set(saveEntity, saveEntity.getFdId());
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return saveEntity;
    }

    @Override
    @Transactional
    public E update(E entity) {
        E old = get(IdNameVO.of(entity.getFdId()));
        BeanCopyUtil.copyProperties(entity, old);
        beforeCreateOrUpdate(old, false);
        iRepository.save(old);
        return old;
    }

    public long count(QueryContext<E> queryContext) {
        return iRepository.count(queryContext.getSpec());
    }

    @Override
    public long count(QueryModel queryModel) {
        initQM(queryModel);
        return count(QueryContext.build(em, entityClass, this, queryModel, voClass));
    }

    public Page<V> findByQueryContext(QueryContext<E> queryContext) {
        QueryModel queryModel = queryContext.getQueryModel();
        // 查询
        TypedQuery<Tuple> query = queryContext.getTypedQuery();
        // 分页
        Pageable pageable = queryContext.getPageable();
        if (pageable.isPaged()) {
            query
                    .setMaxResults(queryModel.getPageSize())
                    .setFirstResult((queryModel.getOffset() - 1) * queryModel.getPageSize());
        }
        // 总数
        long count = this.count(queryContext);
        // 不分页默认查1000
        List<Tuple> tuples = query.getResultList();
        List<V> voList = new ArrayList<>(tuples.size());
        buildVosFromJson(tuples, voList, queryContext);
        return new PageImpl<>(voList, pageable, count);
    }

    @Override
    public Page<V> find(QueryModel queryModel) {
        initQM(queryModel);
        return this.findByQueryContext(QueryContext.build(em, entityClass, this, queryModel, voClass));
    }

    /**
     * 获取数据vo列表
     **/
    private void buildVosFromJson(List<Tuple> tuples, List vos, QueryContext<E> queryContext) {
        tuples.forEach(tuple -> {
            JSONObject jsonObject = queryContext.getJSONObject();
            jsonObject.forEach((k, v) -> jsonObject.put(k, tuple.get((int) v)));
            List<SubFieldModel<E>> childColumns = queryContext.getChildColumns();
            if (!CollectionUtils.isEmpty(childColumns)) {
                for (SubFieldModel<E> childColumn : childColumns) {
                    jsonObject.put(
                            childColumn.getCurrentFieldName(),
                            childColumn.getData(tuple.get(0))
                    );
                }
            }
            vos.add(jsonObject.toJavaObject(queryContext.getVoClass()));
        });
    }

    /**
     * 初始化QM
     */
    private void initQM(QueryModel queryModel) {
        if (queryModel.getConditions() != null) {
            queryModel.getConditions().forEach((k, v) -> {
                if ("fdId".equals(k)) {
                    return;
                }
                if (k.contains(".")) {
                    return;
                }
                if (!(v instanceof Map)) {
                    return;
                }
                Map<String, Object> vMap = (Map<String, Object>) v;
                ReflectUtil.invokeEnum(entityClass, k, vMap);
            });
        }
        if (CollectionUtils.isEmpty(queryModel.getSorts())) {
            queryModel.addSort("fdCreateTime", Sort.Direction.DESC);
        }
    }

    /**
     * 获取所有待删除路径
     **/
    @Override
    public void getFileRemoveList(E old, List<String> toRemoveList) {
        Arrays.asList(entityClass.getDeclaredFields()).forEach(field -> {
            try {
                field.setAccessible(true);
                if (old == null || !field.getName().endsWith("Path")) {
                    return;
                }
                String oldFilePath = (String) field.get(old);
                if (StringUtils.isNotBlank(oldFilePath)) {
                    toRemoveList.add(oldFilePath);
                }
            } catch (IllegalAccessException e) {
                log.error("更新或删除时移除文件出错,获取字段时出错:{},{}", old.getFdId(), field.getName());
            }
        });
    }

    /**
     * 文件移除实际操作
     */
    protected void doFileRemove(List<String> toRemoveList) {
        try {
            iFileService.fileRemove(toRemoveList);
        } catch (Exception e) {
            log.error("更新或删除时移除文件出错,路径为:{}", toRemoveList);
        }
    }

}
