package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 实例化策略 顶层接口
 * AbstractAutowiredCapableBF 会依赖该接口的实现类，因为前者具备实例化 bean 的能力。正因如此，也需要
 * 一个实例化的顶级接口。
 */
public interface InstantiationStrategy {
    Object instantiate(BeanDefinition bd, String beanName, Constructor<?> ctor,Object[] args) throws BeansException;
}
