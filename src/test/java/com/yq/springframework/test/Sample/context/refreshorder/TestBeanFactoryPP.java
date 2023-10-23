package com.yq.springframework.test.Sample.context.refreshorder;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.yq.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class TestBeanFactoryPP implements BeanFactoryPostProcessor {

    StringBuilder sb;

    public TestBeanFactoryPP(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        sb.append("6");
        System.out.println("bfPP_bean append");
    }
}
