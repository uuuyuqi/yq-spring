package com.yq.springframework.test.Sample.context.refreshstep1;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.MutablePropertyValues;
import com.yq.springframework.beans.factory.config.BeanDefinition;
import com.yq.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.yq.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.yq.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import com.yq.springframework.test.Sample.beans.TestBean;

public class TestBdrPP1 implements BeanDefinitionRegistryPostProcessor {

    public static final String INFO = "111wyq";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BeanDefinition yqBD = new BeanDefinition(TestBean.class);
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue("id",111);
        pvs.addPropertyValue("name","wyq");
        yqBD.setPropertyValues(pvs);
        registry.registerBeanDefinition(INFO,yqBD);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

}
