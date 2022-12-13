package com.yq.springframework.beans.factory;

import com.yq.springframework.beans.BeansException;

/**
 * 存储 BD 时出现的异常，继承 BeansException
 * 也是一个运行时异常
 */
public class BeanDefinitionStoreException extends BeansException {

    /**
     * BD 来自于哪个 Resource
     */
    private final String resourceDescription;

    /**
     * bean name
     */
    private final String beanName;


    public BeanDefinitionStoreException(String msg) {
        super(msg);
        this.resourceDescription = null;
        this.beanName = null;
    }

    public BeanDefinitionStoreException(String msg, Throwable cause) {
        super(msg, cause);
        this.resourceDescription = null;
        this.beanName = null;
    }

    public BeanDefinitionStoreException(String resourceDescription, String msg) {
        super(msg);
        this.resourceDescription = resourceDescription;
        this.beanName = null;
    }

    public BeanDefinitionStoreException(String resourceDescription, String msg, Throwable cause) {
        super(msg, cause);
        this.resourceDescription = resourceDescription;
        this.beanName = null;
    }

    public BeanDefinitionStoreException(String resourceDescription, String beanName, String msg) {
        this(resourceDescription, beanName, msg, null);
    }

    public BeanDefinitionStoreException(
            String resourceDescription,
            String beanName,
            String msg,
            Throwable cause) {

        super("Invalid bean definition with name '" + beanName + "' defined in " + resourceDescription + ": " + msg,
                cause);
        this.resourceDescription = resourceDescription;
        this.beanName = beanName;
    }


}
