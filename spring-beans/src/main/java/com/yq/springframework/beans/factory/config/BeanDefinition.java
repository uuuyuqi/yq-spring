package com.yq.springframework.beans.factory.config;


import com.yq.springframework.beans.MutablePropertyValues;
import com.yq.springframework.beans.PropertyValues;

/**
 * Bean 的定义元信息
 * 源码中 BeanDefinition 是个接口，基础功能在 AbstractBeanDefinition 中
 * 此处简单实现直接实现成了类
 */
public class BeanDefinition {

    /**
     * 后面主要用来反射创建bean
     */
    private Class<?> beanClass;

    /**
     * 存储该 bean 的属性和属性值信息
     */
    private MutablePropertyValues propertyValues;

    public BeanDefinition(){
        this(null,null);
    }

    public BeanDefinition(Class<?> beanClass){
        this(beanClass,new MutablePropertyValues());
    }

    public BeanDefinition(Class<?> beanClass,MutablePropertyValues pvs){
        this.beanClass = beanClass;
        this.propertyValues = pvs;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }


    public MutablePropertyValues getPropertyValues() {
        if (this.propertyValues == null) {
            this.propertyValues = new MutablePropertyValues();
        }
        return this.propertyValues;
    }

    public boolean hasPropertyValues() {
        return (this.propertyValues != null && !this.propertyValues.isEmpty());
    }
}
