package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanDefinition;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy{
    @Override
    public Object instantiate(BeanDefinition bd, String beanName, Constructor<?> ctor, Object[] args) throws BeansException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bd.getBeanClass());
        // NoOp: 使用此Enhancer回调的方法将直接委托给基类中的默认实现, 个人理解就是无需方法代理增强
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        if (ctor == null)
            return enhancer.create();
        return enhancer.create(ctor.getParameterTypes(),args);
    }
}
