package com.yq.springframework.beans;

import com.yq.springframework.beans.factory.config.BeanDefinition;


public class BeanInstantiationException extends BeansException{

    public BeanInstantiationException(Class<?> beanClass, String msg) {
        this(beanClass, msg, null);
    }

    public BeanInstantiationException(Class<?> beanClass, String msg, Throwable cause) {
        super("Failed to instantiate [" + beanClass.getName() + "]: " + msg, cause);
    }

    /**
     * 在 spring 源码中，这个方法的参数列表实际上是 (Constructor<?> constructor, String msg, Throwable cause)
     * 然后使用 constructor.getDeclaringClass().getName() 获取 class Name，这里为了方便直接传入 bd 好了
     *
     * 虽然方便，但是这里和 spring 源码的设计上有出入，源码这个地方使用 Constructor 作为参数，可能实际上 spring 源码的设计思路是，这个 bean 未必是
     * 通过 bd 来创建的，反而使用 Constructor 更具备通用性！ 所以直接使用 bd 可能将来会出现高层依赖底层的情况 —— 发生了【依赖倒置】 ！
     *
     * 不过本人只是学习过程中，写轮子，直接用 bd 可能更符合当前项目的需求，没必要过度设计，即也是满足 KISS法则、YAGNI 法则的
     *
     * @param bd beanDefinition
     * @param msg exception msg
     * @param cause exception cause
     */
    public BeanInstantiationException(BeanDefinition bd, String msg, Throwable cause) {
        super("Failed to instantiate [" + bd.getBeanClass() + "]: " + msg, cause);
    }
}
