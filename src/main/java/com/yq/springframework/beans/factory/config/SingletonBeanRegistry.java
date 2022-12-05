package com.yq.springframework.beans.factory.config;


/**
 * 单例 bean 注册器
 *
 * 后面 DefaultListableBF 的 单例注册能力就来自于该类
 *
 * 是单例 bean 的注册，以及从单例池中获取单例的顶层接口
 */
public interface SingletonBeanRegistry {

    /**
     *
     * 将 bean 放入单例池，该方法在源码中实际上基本没发挥什么作用
     * 一般将 bean 放入单例池，使用的都是 addSingleton()， 将单例放入一级缓存中
     * 调用链路:
     * getBean() --> doGetBean --> getSingleton()                   \--> addSingleton()
     *                                  \--> createBean() --> doCreateBean()
     *
     * </pre>
     * @param beanName bean名称
     * @param singleton bean对象
     */
    void registerSingleton(String beanName, Object singleton);

    /**
     * 从单例池中获取单例
     * @param beanName bean名称
     * @return bean对象
     */
    Object getSingleton(String beanName);

}
