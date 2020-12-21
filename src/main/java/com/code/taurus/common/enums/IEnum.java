package com.code.taurus.common.enums;

import java.io.Serializable;

/**
 * 枚举通用接口
 * >> 业务的枚举必须实现该接口
 *
 * @param <V>
 */

public interface IEnum<V> extends Serializable {

    V getValue();

    default boolean is(Object value) {
        return this.getValue().equals(value);
    }

    static <V, E extends IEnum<V>> E valueOf(Class<E> enumType, Object value) {
        for (E en : enumType.getEnumConstants()) {
            if (en.is(value)) {
                return en;
            }
        }
        return null;
    }

}
