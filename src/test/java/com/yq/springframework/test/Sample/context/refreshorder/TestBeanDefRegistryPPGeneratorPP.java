package com.yq.springframework.test.Sample.context.refreshorder;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanDefinition;
import com.yq.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.yq.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.yq.springframework.beans.factory.support.BeanDefinitionRegistry;

public class TestBeanDefRegistryPPGeneratorPP implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry)beanFactory;
        registry.registerBeanDefinition("bdrPPGenerated",new BeanDefinition(TestBeanDefRegistryPPGenerated.class));

        System.out.println("bdrPP_generator generating");
    }
}
