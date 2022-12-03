package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.BeanInstantiationException;
import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * 简单实例化策略，默认情况下就是 JDK 实例化策略
 * 如果发生了方法注入，则就交给子类 Cglib 实例化策略来解决
 * 方法注入可以参考：https://www.jianshu.com/p/619104e43a93
 *
 * 这里为了简单化，没有实现方法注入，此外也没有给 Cglib 和 Simple 两种实例化策略加上父子关系
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy{

    /**
     * 使用已知构造方法创建bean的实例
     * @param bd
     * @param beanName
     * @param ctor 构造方法，注意这个构造方法需要上下文传入，即能事先解析得出
     * @param args
     * @return
     * @throws BeansException
     */
    @Override
    public Object instantiate(BeanDefinition bd, String beanName, Constructor<?> ctor, Object[] args) throws BeansException {
        Class<?> clazz  = bd.getClass();
        try {
            if (ctor != null){
                // 根据传入的构造器的所需的参数列表，找到声明的构造器，然后创建实例
                return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
            }else{
                // 传入的构造器为null，则默认使用无参构造器
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (NoSuchMethodException e) {
            throw new BeanInstantiationException(bd,"No such constructor found",e);
        } catch (InvocationTargetException e) {
            throw new BeanInstantiationException(bd,"invoke constructor failed",e);
        } catch (InstantiationException e) {
            throw new BeanInstantiationException(bd,"instantiate class failed",e);
        } catch (IllegalAccessException e) {
            throw new BeanInstantiationException(bd,"this constructor cannot access",e);
        }
    }
}
