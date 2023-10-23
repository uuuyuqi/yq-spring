package com.yq.springframework.beans.factory.config;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.BeanFactory;

/**
 * 具备自动注入功能的 BeanFactory， 相对于父类 BeanFactory，该类增加了自动注入的功能
 * 但是在 yq-spring，本类的功能简化了自动注入这块的功能
 *
 * 但是本类仍然声明了 2 个非常重要的扩展点方法 —— 开启初始化前/后处理
 * - applyBeanPostProcessorsBeforeInitialization
 * - applyBeanPostProcessorsAfterInitialization
 *
 *
 *
 * 比较奇怪的是，在 spring 源码中，这个类也定义了 createBean Bean 方法，并且很少在 web-mvc 以外的场景使用！
 * 在 spring 中的 createBean 基本都来自于 AbstractBeanFactory 类中声明的 createBean 方法！
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 初始化【前】处理
     * @param existingBean 现有的待处理的 bean
     * @param beanName bean 名称
     * @return 处理后的 bean
     * @throws BeansException ex
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException;


    /**
     * 初始化【后】处理
     * @param existingBean 现有的待处理的 bean
     * @param beanName bean 名称
     * @return 处理后的 bean
     * @throws BeansException ex
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;
}
