package com.code.taurus.common.jpa;

import com.code.taurus.common.entity.AbstractEntity;
import com.code.taurus.common.enums.ConditionEnum;
import com.code.taurus.common.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 帮助类: SQL条件拼接
 *
 * @author 郑楷山
 **/

@SuppressWarnings("unchecked")
public class SpecUtil {

    public static <E extends AbstractEntity> Specification<E> getSpec(Class<E> entityClass, String name, String condition, Object value) {
        String[] split = name.split("\\.");
        if (split.length == 0L) {
            return null;
        }
        if (null == value) {
            if (!condition.equals(ConditionEnum.NOT_NULL.getValue().toUpperCase(Locale.US))
                    || !condition.equals(ConditionEnum.NULL.getDescription().toUpperCase(Locale.US))) {
                return null;
            }
        }

        if (condition.equals(ConditionEnum.MULTI_AND.getValue())) {
            return multi(entityClass, split, value, true);
        } else if (condition.equals(ConditionEnum.MULTI_OR.getValue())) {
            return multi(entityClass, split, value, false);
        }
        return (Specification<E>) (root, criteriaQuery, criteriaBuilder) ->
                buildPredicate(condition, criteriaBuilder, buildPath(root, criteriaQuery, entityClass, split), value);
    }

    // multi
    private static <E extends AbstractEntity> Specification<E> multi(Class<E> entityClass, String[] split, Object value, boolean isAnd) {
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            return (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (StringUtils.isBlank(entry.getKey()) || entry.getValue() == null) {
                        continue;
                    }
                    predicates.add(
                            buildPredicate(entry.getKey(), criteriaBuilder, buildPath(root, criteriaQuery, entityClass, split), entry.getValue())
                    );
                }
                Predicate[] predicateArray = new Predicate[predicates.size()];
                if (isAnd) {
                    criteriaQuery.where(criteriaBuilder.and(predicates.toArray(predicateArray)));
                } else {
                    criteriaQuery.where(criteriaBuilder.or(predicates.toArray(predicateArray)));
                }
                return criteriaQuery.getRestriction();
            };
        } else {
            return null;
        }
    }

    private static Path<Object> buildPath(Root<?> root, CriteriaQuery<?> cq, Class<?> aClass, String[] split) {
        if (split.length == 0) {
            return null;
        } else if (split.length == 1) {
            return root.get(split[0]);
        }
        Field field = ReflectUtil.getField(aClass, split[0]);
        boolean isOne = field != null && ReflectUtil.isOne(field);
        // isOne
        if (isOne) {
            return root.get(split[0]).get(split[1]);
        }
        // isMany
        Join<Object, Object> join = root.join(split[0], JoinType.INNER);
        return join.get(split[1]);
    }

    private static Predicate buildPredicate(String condition, CriteriaBuilder criteriaBuilder, Path<Object> path, Object value) {
        if (path == null) {
            return null;
        }
        switch (condition.toUpperCase(Locale.US)) {
            case "EQUAL":
                return equal(criteriaBuilder, path, value);
            case "NOT_EQUAL":
                return notEqual(criteriaBuilder, path, value);
            case "IN":
                return in(path, value);
            case "NOT_IN":
                return notIn(path, value);
            case "LIKE_BOTH":
                return likeBoth(criteriaBuilder, path, value);
            case "GT":
                return gt(criteriaBuilder, path, value);
            case "GT_EQUAL":
                return gtEqual(criteriaBuilder, path, value);
            case "LT":
                return lt(criteriaBuilder, path, value);
            case "LT_EQUAL":
                return ltEqual(criteriaBuilder, path, value);
            case "NULL":
                return isNull(path);
            case "NOT_NULL":
                return notNull(path);
            default:
                return null;
        }
    }

    /**************基础操作***************/

     /* // exists
    private static Predicate exists(CriteriaBuilder cb, Path<Object> path, Object value) {
        return cb.exists(cb.equal((Expression) path, value));
    }

    // not exists
    private static Predicate notIn(CriteriaBuilder cb, Path<Object> path, Object value) {
        return cb.exists((Expression) path.in(value)).not();
    }*/

    // like-both
    private static Predicate likeBoth(CriteriaBuilder cb, Path<Object> path, Object value) {
        if (value instanceof String) {
            String val = (String) value;
            if (StringUtils.isBlank(val)) {
                return null;
            }
            return cb.like((Expression) path, "%" + value + "%");
        } else {
            return null;
        }
    }

    // =
    private static Predicate equal(CriteriaBuilder cb, Path<Object> path, Object value) {
        return cb.equal(path, value);
    }

    // !=
    private static Predicate notEqual(CriteriaBuilder cb, Path<Object> path, Object value) {
        return cb.notEqual(path, value);
    }

    // >
    private static Predicate gt(CriteriaBuilder cb, Path<Object> path, Object value) {
        return cb.greaterThan((Expression) path, (Comparable) value);
    }

    // >=
    private static Predicate gtEqual(CriteriaBuilder cb, Path<Object> path, Object value) {
        return cb.greaterThanOrEqualTo((Expression) path, (Comparable) value);
    }

    // <
    private static Predicate lt(CriteriaBuilder cb, Path<Object> path, Object value) {
        return cb.lessThan((Expression) path, (Comparable) value);
    }

    // <=
    private static Predicate ltEqual(CriteriaBuilder cb, Path<Object> path, Object value) {
        return cb.lessThanOrEqualTo((Expression) path, (Comparable) value);
    }

    // in
    private static Predicate in(Path<Object> path, Object value) {
        if (value instanceof List) {
            List<Long> list = (List<Long>) value;
            return path.in(list);
        }
        return null;
    }

    // not in
    private static Predicate notIn(Path<Object> path, Object value) {
        if (value instanceof List) {
            List<Long> list = (List<Long>) value;
            return path.in(list).not();
        }
        return null;
    }

    // is null
    private static Predicate isNull(Path<Object> path) {
        return path.isNull();
    }

    // is not null
    private static Predicate notNull(Path<Object> path) {
        return path.isNotNull();
    }

}