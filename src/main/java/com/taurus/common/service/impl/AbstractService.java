package com.taurus.common.service.impl;

import com.taurus.common.config.security.UserContextHelper;
import com.taurus.common.entity.AbstractEntity;
import com.taurus.common.enums.ExceptionEnum;
import com.taurus.common.model.*;
import com.taurus.common.repository.IRepository;
import com.taurus.common.service.IFileService;
import com.taurus.common.service.IService;
import com.taurus.common.util.BeanUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Date;

/**
 * 服务: 基础类实现
 *
 * @author 郑楷山
 **/
public abstract class AbstractService<T extends AbstractEntity, V extends AbstractVO> implements IService<T, V> {

    @Autowired
    protected IRepository<T> iRepository;
    @Autowired
    private IFileService fileService;
    @PersistenceContext
    private EntityManager em;

    private final Class<T> entityClass;

    public AbstractService(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T getById(Long id) {
        return iRepository.findById(id).orElseThrow(NoRecordException::new);
    }

    @Override
    public Page<T> list(QueryModel queryModel) {
        return iRepository.list(queryModel, entityClass, em);
    }

    @SneakyThrows
    @Override
    @Transactional
    public T create(V vo) {
        T entity;
        try {
            entity = entityClass.newInstance();
            BeanUtil.copyProperties(vo, entity);
            Date date = new Date();
            SysSecurityUser user = UserContextHelper.getUser();
            entity.setFdCreateBy(user == null ? "" : user.getUsername());
            entity.setFdCreateTime(date);
            entity.setFdUpdateBy(user == null ? "" : user.getUsername());
            entity.setFdUpdateTime(date);
            entity.setFdEnabled(Boolean.TRUE);
            entity.setFdDeleted(Boolean.FALSE);
            entity = iRepository.save(entity);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BusinessException(ExceptionEnum.CLASS_NOT_FOUND_ERROR);
        }
        return entity;
    }

    @Override
    @Transactional
    public T update(V vo) {
        T entity = getById(vo.getFdId());
        BeanUtil.copyProperties(vo, entity);
        SysSecurityUser user = UserContextHelper.getUser();
        entity.setFdUpdateBy(user == null ? "" : user.getUsername());
        entity.setFdUpdateTime(new Date());
        T newEntity = iRepository.save(entity);
        fileRemove(entity, newEntity);
        return newEntity;
    }

    @Transactional
    public void delete(Long id) {
        iRepository.delete(getById(id));
    }

    @Override
    @Transactional
    public void deleteAll(String ids) {
        if (StringUtils.isBlank(ids)) {
            return;
        }
        String[] split = ids.split(",");
        if (split.length == 0) {
            return;
        }
        Arrays.stream(split).forEach(id -> delete(Long.valueOf(id)));
    }

    // 文件移除
    public void fileRemove(T entity, T newEntity) {
        Arrays.asList(entityClass.getDeclaredFields()).forEach(field -> {
            try {
                field.setAccessible(true);
                if (field.getName().contains("Path")) {
                    String filePath = (String) field.get(entity);
                    if (StringUtils.isBlank(filePath)) {
                        return;
                    }
                    if (newEntity != null && filePath.equals(field.get(newEntity))) {
                        return;
                    }
                    fileService.fileRemove((String) field.get(entity));
                }
            } catch (IllegalAccessException ignored) {
            }
        });
    }

}
