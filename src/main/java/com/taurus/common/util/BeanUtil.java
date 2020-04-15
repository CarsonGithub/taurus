package com.taurus.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

/**
 * 帮助类: 自定义bean帮助类
 *
 * @author 郑楷山
 **/

public class BeanUtil {

    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target,getNullPropertyNames(source));
    };

    /**
     * 获取空值的字段名
     * @param source
     * @return
     */
    private static String[] getNullPropertyNames (Object source) {
        final BeanWrapper wrapper = new BeanWrapperImpl(source);
        return Stream.of(wrapper.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrapper.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}
