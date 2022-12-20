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
     * @param type bean 类型
     * @return bean names
     */
    String[] getBeanNamesForType(Class<?> type);
}
