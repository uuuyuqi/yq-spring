package com.yq.springframework.beans.factory.config;


/**
 * Bean 的定义元信息
 */
public class BeanDefinition {

    // 后面主要用来反射创建bean
    private Class<?> beanClass;

    public BeanDefinition(Class<?> beanClass){
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
}
