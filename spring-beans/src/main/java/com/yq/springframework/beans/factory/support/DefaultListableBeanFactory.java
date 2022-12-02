package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.NoSuchBeanDefinitionException;
import com.yq.springframework.beans.factory.config.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 永远的主角！
 *
 * 是狭义上 spring 容器（虽然单例池更合适被认为是狭义上的IoC容器）的默认实现
 * 具备了祖先继承下来的 2 大核心功能
 * 1. 来自祖先 BF 接口的 —— getBean 的功能
 * 2. 来自祖先 SingletonBeanRegistry 的单例 bean 注册功能（以及维护功能）
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry{


    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /**
     * 由于继承了 BF，而 BF 需要 getBD的能力(因为 BF 在 getBean 获取不到的情况下，需要创建 Bean，这个时候就用到了 getBD 方法)
     * 而 getBD 是 BDRegistry 封装的能力
     * @param beanName BD 的 beanName
     * @return BD
     * @throws NoSuchBeanDefinitionException bean不存在
     */
    @Override
    protected BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException(beanName);
        }
        return beanDefinition;
    }

    /**
     * 此外，DLBF 还应该具备注册 BD 的能力
     * @param beanName BD 的 beanName
     * @param beanDefinition BD
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }

}
