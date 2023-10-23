package com.yq.springframework.test.Sample.context.common;

import cn.hutool.core.util.StrUtil;
import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanPostProcessor;

public class TestBeanPostProcessor implements BeanPostProcessor {

    private final String name;

    public TestBeanPostProcessor(String name) {
        this.name = name;
        System.out.println(StrUtil.format("TestBeanPostProcessor[{}] was created!",name));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(StrUtil.format("(before initialization)now the bean [{}] was processed By BeanPP[{}]",beanName,name));
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(StrUtil.format("(after initialization)now the bean [{}] was processed By BeanPP[{}]",beanName,name));
        return bean;
    }
}
