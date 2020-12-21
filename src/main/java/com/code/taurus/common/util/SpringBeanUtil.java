package com.code.taurus.common.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * 帮助类: Bean工厂管理
 *
 * @author 郑楷山
 **/
@Component
@SuppressWarnings("unchecked")
public class SpringBeanUtil implements BeanFactoryAware {

    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringBeanUtil.beanFactory = beanFactory;
    }

    public static <T> T getBean(String beanName) {
        if (null != beanFactory) {
            return (T) beanFactory.getBean(beanName);
        }
        return null;
    }

    public static <T> T getBean(Class<?> aClass) {
        if (null != beanFactory) {
            return (T) beanFactory.getBean(aClass);
        }
        return null;
    }
}
