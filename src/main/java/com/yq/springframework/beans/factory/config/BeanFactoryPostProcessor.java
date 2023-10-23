package com.yq.springframework.beans.factory.config;

import com.yq.springframework.beans.BeansException;

/**
 * BeanFactoryPostProcessor
 *
 * 可以对 beanFactory 进行一些自定义的、修饰性的后处理
 */
public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
