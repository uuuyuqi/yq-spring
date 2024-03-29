package com.yq.springframework.beans.factory.support;

import cn.hutool.core.lang.Assert;
import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.BeanFactory;
import com.yq.springframework.beans.factory.config.BeanDefinition;
import com.yq.springframework.beans.factory.config.BeanPostProcessor;
import com.yq.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BF 的直接实现，是个抽象类
 * 是 DLBF 的父类
 * 为后续子类 BF 提供一套 getBean 的模板
 *
 * 这个类的功能主要是：
 * 1. 是 BF 接口的直接继承，提供了 getBean 方法的模板 —— 先查单例池 —— 查不到就获取BD、走创建 bean 的流程
 * 2. 获取 BD（由于功能 1 需要获取BD，所以本类应该有获取BD行为）
 * 3. 创建 Bean
 *
 * 实际上 spring 源码中，对该类的描述也是，主要是需要告诉子类，需要去实现(1)获取BD (2)创建Bean
 * 该类的直接子类其实就 1 个 —— AbstractAutowireCapableBeanFactory
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    /**
     * 父类工厂
     * 实际上在实践过程中，也可以是一个 spring 上下文
     */
    private BeanFactory parentBeanFactory;

    /**
     * bean post processors
     *
     * spring 源码此处是 CopyOnWriteList
     */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    public AbstractBeanFactory() {
    }

    public AbstractBeanFactory(BeanFactory parentBeanFactory) {
        this.parentBeanFactory = parentBeanFactory;
    }

    public void setParentBeanFactory(BeanFactory parentBeanFactory) {
        if (this.parentBeanFactory != null && this.parentBeanFactory != parentBeanFactory) {
            throw new IllegalStateException("Already associated with parent BeanFactory: " + this.parentBeanFactory);
        }
        this.parentBeanFactory = parentBeanFactory;
    }


    @Override
    public Object getBean(String beanName) {
        return getBean(beanName, null);
    }

    /**
     * 这个地方是非抽象的（剩余方法都是抽象法，下沉到具体子类来实现），因为这个地方其实就是模板方法
     * 为后续子类提供好主线的 getBean 的流程，子类需要个性化实现的子流程，会在父类抽象类中，以抽象方法的形式提供
     * @param beanName beanName
     * @param args bean 的构造器参数
     * @return bean对象
     * @throws BeansException 异常
     */
    @Override
    public Object getBean(String beanName, Object... args) throws BeansException {
        // getSingleton方法 来自于默认单例注册器SingletonBeanRegistry
        // 这一步是先去单例池里面获取bean：
        //  - 获取到了，就返回
        //  - 没获取到，就走bean创建的流程
        Object bean = getSingleton(beanName);
        if (bean != null)
            return bean;

        BeanDefinition beanDefinition = getBeanDefinition(beanName);

        return createBean(beanName,beanDefinition,args);
    }

    /**
     * 按照 beanName 查询 bd
     * @param beanName beanName
     * @return 按 beanName 查到的 bd
     * @throws BeansException ex
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 根据 bd，创建一个名字为 beanName bean
     * @param beanName 创建后的 bean 名称
     * @param beanDefinition bean创建依据
     * @return bean实例对象
     * @throws BeansException 创建 bean 异常
     */
    protected abstract Object createBean(String beanName,BeanDefinition beanDefinition,Object[] args) throws BeansException;


    /**
     * 获取父 BF
     * 是对于 HierarchicalBeanFactory 的实现
     * @return 父 BF
     */
    public BeanFactory getParentBeanFactory() {
        return this.parentBeanFactory;
    }


    /**
     * 给 BF 添加 bean 处理器
     *
     * @param beanPostProcessor bean 处理器
     */
    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
        this.beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * 获取当前 BF 中持有的 BeanPostProcessors
     * @return beanPostProcessors
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

}
