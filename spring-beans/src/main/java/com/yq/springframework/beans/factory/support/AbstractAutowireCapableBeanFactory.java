package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.BeanInstantiationException;
import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * 该类是最终主角 DLBF 的父亲，是 AbstractBF 类的 直接继承人
 * 实际上 AbstractBF 可以直接生个儿子，就是 DLBF。 但是实际上的继承关系并非如此！
 * 因为 spring 框架需具备自动注入功能的，此时父亲 AbstractBF 此时和自动注入功能还没半毛钱关系的
 * 如果让默认实现类 DLBF 直接继承AbstractBF，并拥有了自动注入功能（缺少AbstractAutowireCapableBeanFactory过度的情况下）。
 * 实际上是儿子的功能是超出了父亲的职责的，也就是违背了【里式替换原则】。
 * 该原则强调了父子在功能上应该协调，儿子应该是强化细节，不要直接空穴来风增加功能
 *
 *
 * 实际上这一层相当于是 抽象BF类 的增强抽象，开启了 BF 具备自动注入功能的主线任务！
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
    protected InstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }

    /**
     * 对默认 AbstractBF 的增强，增加了可以自动注入的功能
     * 在 spring 源码中，该方法是本类的核心方法，提供了：bean实例创建、bean属性注入、发起beanPP处理等功能
     * @param beanName 创建后的 bean 名称
     * @param beanDefinition bean创建依据
     * @return bean
     * @throws BeansException bean 创建异常
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;

        // spring 源码中，此处会进行 bean class 的解析和加载

        // 此外，真正的实例创建需要先委托 createBeanInstance()  方法
        // createBean() -> doCreateBean() -> createBeanInstance() -> instantiateBean() -> instantiationStrategy.instantiate()
        // 重点在于，createBeanInstance() 方法中需要作出判断 bean 的创建方式:
        //      1.factoryMethod
        //      2.利用构造器反射创建
        // 如果是后者，则需要考虑选用哪个构造器

        try{
            // 解析 bean 的构造器，使用构造器创建 bean
            bean = createBeanInstance(beanName, beanDefinition, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 创建 bean 完成后，将 bean 放入单例池
        // 该方法由祖先 SingletonBeanRegistry 提供
        registerSingleton(beanName, bean);

        return bean;
    }

    /**
     * 解析出该 bean 的构造器，然后将实例化任务委托给 实例化策略类 （这里直接是Cglibsubclassing策略）
     * @param beanName bean name
     * @param bd bean definition
     * @param args bean 实例化时的 构造方法参数
     * @return 实例化出来的 bean 对象
     */
    protected Object createBeanInstance(String beanName, BeanDefinition bd, Object[] args){
        Constructor<?> constructorToUse;
        Class<?> beanClass = bd.getClass();
        if (beanClass.isInterface()) {
            throw new BeanInstantiationException(beanClass, "Specified class is an interface");
        }


        // 根据 args 分析出要使用的构造器
        Class<?>[] paramTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
        }

        try {
            Constructor<?> ctor = beanClass.getDeclaredConstructor(paramTypes);
            return getInstantiationStrategy().instantiate(bd,beanName,ctor,args);
        } catch (NoSuchMethodException e) {
            throw new BeanInstantiationException(bd,"无法根据提供的参数找到合适的构造器",e);
        }


    }
}
