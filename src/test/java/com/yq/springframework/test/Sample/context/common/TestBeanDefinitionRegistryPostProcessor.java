package com.yq.springframework.test.Sample.context.common;

import cn.hutool.core.util.StrUtil;
import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.yq.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.yq.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

public class TestBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private final String name;

    public TestBeanDefinitionRegistryPostProcessor(String name) {
        this.name = name;
        System.out.println(
                StrUtil.format("TestBeanDefinitionRegistryPostProcessor[{}] was created!",name)
        );
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println(
                StrUtil.format(
                        "(after created)now the beanFactory [{}] was processed By BDRPP[{}]",
                        beanFactory.getClass().getSimpleName(),name
                )
        );
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println(
                StrUtil.format(
                        "(after created)now the beanDef registry [{}] was processed By BDRPP[{}]",
                        registry.getClass().getSimpleName(),name
                )
        );
    }
}
