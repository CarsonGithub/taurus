package com.code.taurus.common.util;

import com.code.taurus.common.entity.AbstractEntity;
import com.code.taurus.common.enums.ExceptionEnum;
import com.code.taurus.common.model.AbstractVO;
import com.code.taurus.common.model.BusinessException;
import com.code.taurus.common.enums.IEnum;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 帮助类: 反射帮助类
 *
 * @author 郑楷山
 **/

@Slf4j
public class ReflectUtil {

    public static boolean isOne(Field field) {
        Class<?> type = field.getType();
        if (null == type.getSuperclass()) {
            return false;
        }
        return type.getSuperclass().equals(AbstractVO.class);
    }

    /**
     * 判断是否集合字段
     */
    public static boolean isMany(Field field) {
        Class<?> type = field.getType();
        return type.equals(List.class) || type.equals(Set.class);
    }

    /**
     * 获取字段
     */
    public static Field getField(Class<?> aClass, String name) {
        try {
            return aClass.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            log.error("获取不到字段:" + name);
        }
        return null;
    }

    /**
     * 根据Field获取泛型class
     */
    public static Optional<Class<?>> getGenericTypeByClass(Class<?> aClass, String name) {
        Field field = getField(aClass, name);
        if (field == null || !isMany(field)) {
            return Optional.empty();
        }
        return getGenericTypeByField(field);
    }

    /**
     * 根据Field获取泛型class
     */
    @SuppressWarnings("unchecked")
    public static Optional<Class<?>> getGenericTypeByField(Field field) {
        // 当前集合的泛型类型
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            try {
                // 得到泛型里的class类型对象
                return Optional.of((Class<?>) pt.getActualTypeArguments()[0]);
            } catch (Exception e) {
                log.error("集合中非业务实体:" + field.getName());
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * 枚举valueOf方法
     **/
    public static <E extends AbstractEntity> void invokeEnum(Class<E> entityClass, String key, Map<String, Object> conditionVal) {
        try {
            Field field = entityClass.getDeclaredField(key);
            field.setAccessible(true);
            Class<?> aClass = field.getType();
            if (!(aClass.equals(Enum.class))) {
                return;
            }
            for (Type anInterface : aClass.getGenericInterfaces()) {
                if (!(anInterface instanceof IEnum)) {
                    continue;
                }
                Method method = aClass.getMethod("valueOf", String.class);
                for (Map.Entry<String, Object> entry : conditionVal.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        String val = (String) entry.getValue();
                        conditionVal.put(entry.getKey(), method.invoke(aClass, val));
                    }
                }
                break;
            }
        } catch (NoSuchFieldException ignore) {
        } catch (Exception e) {
            log.info("反射枚举方法异常:{}", e.getMessage());
            throw new BusinessException(ExceptionEnum.SERVER_ERROR);
        }
    }

    /**
     * 获取实体对应字段名的字段值
     **/
    public static <E extends AbstractEntity> String getFieldValue(E entity, String fieldName) {
        String value = null;
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            value = (String) field.get(entity);
        } catch (NoSuchFieldException e) {
            throw new BusinessException(ExceptionEnum.NO_FIELD_ERROR);
        } catch (IllegalAccessException ignore) {
        }
        return value;
    }

    /**
     * 获取实体所有字段
     **/
    public static List<String> getOneOrManyNameList(List<String> fieldList, Class<?> clazz, boolean isOne, boolean isMany) {
        if (clazz == null) {
            return fieldList;
        }
        if (!isOne && !isMany) {
            return fieldList;
        }
        Arrays.asList(clazz.getDeclaredFields()).forEach(field -> {
            if ((isOne && isOne(field)) || (isMany && isMany(field))) {
                field.setAccessible(true);
                fieldList.add(field.getName());
            }

        });
        return getOneOrManyNameList(fieldList, clazz.getSuperclass(), isOne, isMany);
    }

    /**
     * 获取实体所有字段
     **/
    public static List<String> getFieldNameList(List<String> fieldList, Class<?> clazz, boolean ignoreOne, boolean ignoreMany) {
        if (clazz == null) {
            return fieldList;
        }
        Arrays.asList(clazz.getDeclaredFields()).forEach(field -> {
            if (ignoreOne && isOne(field)) {
                return;
            }
            if (ignoreMany && isMany(field)) {
                return;
            }
            field.setAccessible(true);
            fieldList.add(field.getName());
        });
        return getFieldNameList(fieldList, clazz.getSuperclass(), ignoreOne, ignoreMany);
    }

    /**
     * 获取实体所有字段
     **/
    public static List<Field> getFieldList(List<Field> fieldList, Class<?> clazz, boolean ignoreOne, boolean ignoreMany) {
        if (clazz == null) {
            return fieldList;
        }
        Arrays.asList(clazz.getDeclaredFields()).forEach(field -> {
            if (ignoreOne && isOne(field)) {
                return;
            }
            if (ignoreMany && isMany(field)) {
                return;
            }
            field.setAccessible(true);
            fieldList.add(field);
        });
        return getFieldList(fieldList, clazz.getSuperclass(), ignoreOne, ignoreMany);
    }

}
