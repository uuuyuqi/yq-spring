package com.yq.springframework.test.Sample.context.refreshorder;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.yq.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class TestBeanDefRegistryPP implements BeanFactoryPostProcessor {

    StringBuilder sb;

    public TestBeanDefRegistryPP(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        sb.append("2");
        System.out.println("bfPP_bean append");
    }
}
