package com.yq.springframework.beans.factory.config;

import com.yq.springframework.beans.factory.HierarchicalBeanFactory;

/**
 * 可配置化 beanFactory
 *
 * 功能如下：
 * - 支持 BF 的配置 （比如可以往 BF 中添加 BeanPostProcessor）
 * - 具备父子层级关系
 * - 单例获取、维护功能
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    /**
     * 给 BF 添加 bean 处理器
     * @param beanPostProcessor bean 处理器
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
