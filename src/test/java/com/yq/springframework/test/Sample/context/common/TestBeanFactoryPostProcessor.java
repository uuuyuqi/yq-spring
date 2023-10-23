package com.yq.springframework.test.Sample.context.common;

import cn.hutool.core.util.StrUtil;
import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.yq.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class TestBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private final String name;

    public TestBeanFactoryPostProcessor(String name) {
        this.name = name;
        System.out.println(
                StrUtil.format("TestBeanFactoryPostProcessor [{}] was created!",name)
        );
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println(
                StrUtil.format(
                        "(after created)now the beanFactory [{}] was processed By BFPP[{}]",
                        beanFactory.getClass().getSimpleName(),name
                )
        );

    }
}
