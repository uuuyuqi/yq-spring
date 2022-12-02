package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;


/**
 * 单例 bean 注册器
 *
 * SingletonBeanRegistry 的 默认实现
 * DefaultListableBF 会间接继承自该类
 *
 *
 * bean 底层的三级缓存就在位于这个地方！！！
 *
 * 注意需要和 BeanDefinitionRegistry 区分, 因为个人理解：
 * - DefaultSingletonBeanRegistry 就是封装了单例池的维护能力，是 IoC 容器的直接实现
 * - BeanDefinitionRegistry 是单纯的 BD 注册中心
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new HashMap<>();


    @Override
    public void registerSingleton(String beanName, Object singleton) {
        singletonObjects.put(beanName, singleton);
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }
}
