package com.yq.springframework.beans.factory;


import com.yq.springframework.beans.BeansException;

import java.util.Map;

/**
 * ListableBeanFactory 在 getBean 时，能够返回【符合条件的多个 bean】 的 BF
 */
public interface ListableBeanFactory extends BeanFactory{

    /**
     * 返回指定类型的所有实例
     * @param type  bean 类型
     * @param <T> bean 类型泛型
     * @return beanName ==> bean (map)
     * @throws BeansException ex
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    /**
     * 返回定义的所有 bean 的名称
     * @return 所有 bean 的名称
     */
    String[] getBeanDefinitionNames();

    /**
     * 按照类型 tyoe 来查找 bean
     *
     * 这个用途大有来头！这个地方非常让人疑惑，为什么还获取 bean name？直接获取 bean 不就好了？
     * 直接用 getBeansOfType 这个方法不就行了？
     *
     * 实际上，直接获取 bean name 的原因是：bean 前后有依赖关系，后面的 bean 在 getBean 时可以需要被前面的 bean 处理 ！
     * 比如：我有两个 bean，他们类型都是 BeanPostProcessor
     * beanA: 是一个 BeanPostProcessor, 专门处理 BeanPostProcessor 的工具 processor
     * beanB: 是一个 BeanPostProcessor, 处理业务 bean 的普通 processor
     * 如果直接按照 BeanPostProcessor 来 getBeansOfType, 会直接同时把 A 和 B 都 getBean 出来! 而获取 names 之后,
     * 可以人为先把 A getBean出来，然后再 getBean B 的时候, 此时 A 已经注册好了, 此时再 getBean B时, B 的 getBean 就会被 A 处理到!
     *
     *
     *
     * @param type bean 类型
     * @return bean names
     */
    String[] getBeanNamesForType(Class<?> type);
}
