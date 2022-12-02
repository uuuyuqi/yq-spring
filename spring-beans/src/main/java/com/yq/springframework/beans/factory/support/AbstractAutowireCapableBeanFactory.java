package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanDefinition;

/**
 * 该类是最终主角 DLBF 的父亲，是 AbstractBF 类的 直接继承人
 * 实际上 AbstractBF 可以直接生个儿子，就是 DLBF。 但是实际上的继承关系并非如此！
 * 因为 spring 框架需具备自动注入功能的，此时父亲 AbstractBF 此时和自动注入功能还没半毛钱关系的
 * 如果让默认实现类 DLBF 直接继承AbstractBF，并拥有了自动注入功能（缺少AbstractAutowireCapableBeanFactory过度的情况下）。
 * 实际上是儿子的功能是超出了父亲的职责的，也就是违背了【里式替换原则】。
 * 该原则强调了父子在功能上应该协调，儿子应该是强化细节，不要直接空穴来风增加功能
 *
 *
 * 实际上这一层相当于是 抽象BF类 的增强抽象，开启了 BF 具备自动注入功能的主线任务！
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    /**
     * 对默认 AbstractBF 的增强，增加了可以自动注入的功能
     * @param beanName 创建后的 bean 名称
     * @param beanDefinition bean创建依据
     * @return bean
     * @throws BeansException
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        Object bean = null;
        try{
            // 默认无参数注入，直接反射创建
            bean = beanDefinition.getBeanClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // 创建 bean 完成后，将 bean 放入单例池
        // 该方法由祖先 SingletonBeanRegistry 提供
        registerSingleton(beanName, bean);

        return bean;
    }
}
