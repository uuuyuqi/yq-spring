package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.factory.config.BeanDefinition;

/**
 * bean 注册器的顶层接口
 *
 * 注意区分：
 * - BeanDefinitionRegistry —— BD holder
 * - SingletonBeanRegistry —— (singleton)Bean holder
 *
 * 这个类目前只规定了 BD注册器 的行为，具体 BD 的保存（BDMap）交给子类 DLBF 来实现
 *
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);


}
