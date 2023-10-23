package com.yq.springframework.beans.factory.config;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.ListableBeanFactory;

/**
 * 可配置化、能获取多个 bean 的 BF，也是我们 DefaultLBF 要继承的父类，这个类更像是一【胶水类】
 *
 * 这个其实已经算是 BF 的终态了, 具备了如下的功能：
 * - 可配置化的 BF
 * - 可罗列多个 Bean 的 BF
 * - 可自动注入的 BF
 *
 *
 * 估计此处 spring 就想把所有的行为包含起来，但是实际上存在着大量重复的继承操作，这个地方设计并不优雅，交叉重复十分严重
 * 比如默认的 DefaultLBF就已经继承了 AbstractBF ，也就早已具备了单例池，而 ConfigurableBeanFactory 又继承了一遍单例池
 */
public interface ConfigurableListableBeanFactory extends
        ConfigurableBeanFactory,
        ListableBeanFactory,
        AutowireCapableBeanFactory {

    /**
     * 加载所有非惰性加载(non lazy-init)的 bean
     * @throws BeansException ex
     */
    void preInstantiateSingletons() throws BeansException;
}
