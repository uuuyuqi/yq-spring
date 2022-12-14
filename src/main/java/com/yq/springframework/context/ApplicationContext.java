package com.yq.springframework.context;

import com.yq.springframework.beans.factory.ListableBeanFactory;

/**
 * spring 上下文顶层接口
 *
 * 在 spring 中，IOC 是 BeanFactory 完成的
 * 而对于一切资源、配置的整合，以及功能上的扩展，都是 ApplicationContext 来完成的
 * 个人觉得，仅仅从 IOC 层面来说，BF 更为重要，AC 更像是对 BF 的一层封装
 */
public interface ApplicationContext extends ListableBeanFactory {
}
