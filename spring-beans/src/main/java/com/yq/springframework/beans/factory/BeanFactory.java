package com.yq.springframework.beans.factory;

/**
 * bean的获取工厂
 * 顶层接口
 */
public interface BeanFactory {

    Object getBean(String beanName);

}
