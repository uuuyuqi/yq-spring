package com.yq.springframework.beans.factory;

import com.yq.springframework.beans.BeansException;

/**
 * bean的获取工厂
 * 顶层接口
 */
public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;

}
