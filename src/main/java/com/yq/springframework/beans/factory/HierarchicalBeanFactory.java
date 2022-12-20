package com.yq.springframework.beans.factory;


/**
 * 层次化 BF 接口 —— 声明了父子工厂的能力
 *
 * 所以说，spring 中的父子容器是组合关系。毕竟经验上来说，【多组合，少继承】
 */
public interface HierarchicalBeanFactory extends BeanFactory {
    /**
     * 获取父工厂
     * @return 父工厂
     */
    BeanFactory getParentBeanFactory();
}
