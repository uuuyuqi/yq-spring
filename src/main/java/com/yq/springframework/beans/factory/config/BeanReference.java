package com.yq.springframework.beans.factory.config;

/**
 * 这个类的作用，主要用在 beanDefinition 的 pvs 里面，
 * 当 value 是一个 BeanReference 时，表示这个 value 实际上是其 ioc 容器中的其他 bean
 *
 * 在 spring 源码中是个借口，这里简单起见，直接用一个类来表示
 */
public class BeanReference {
    private final String beanName;

    public BeanReference(String beanName) {
        if (beanName == null) {
            throw new RuntimeException("必须告知引用的 bean 的名称!");
        }
        this.beanName = beanName;
    }

    public String getBeanName() {
        return this.beanName;
    }
}
