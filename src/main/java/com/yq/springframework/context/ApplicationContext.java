package com.yq.springframework.context;

import com.yq.springframework.beans.factory.HierarchicalBeanFactory;
import com.yq.springframework.beans.factory.ListableBeanFactory;
import com.yq.springframework.core.io.ResourceLoader;

/**
 * spring 上下文顶层接口
 *
 * 提供如下能力：
 * - getBean (对 BF 的包装)
 * - 资源加载能力
 * - 事件发布能力
 * - 上下文层次关系
 *
 * 后续的继承关系：
 * ApplicationContext ===> 顶层接口
 * ConfigurableApplicationContext ===> 次顶层接口，提出了 refresh 方法
 * AbstractApplicationContext ===> *抽象定义了 refresh 模板
 * AbstractRefreshableApplicationContext ===> 抽象定义了 refresh 中 bf 创建的模板
 * AbstractRefreshableConfigApplicationContext ===> 抽象定义了 bf 创建时，资源加载的模板
 * AbstractXmlApplicationContext ===> 抽象定义了 xml 资源的加载模板
 * ClasspathXmlApplicationContext ===> 最终实现了从 classpath 下加载资源，创建BF
 *
 * 在 spring 中，IOC 是 BeanFactory 完成的
 * 而对于一切资源、配置的整合，以及功能上的扩展，都是 ApplicationContext 来完成的
 * 个人觉得，仅仅从 IOC 层面来说，BF 更为重要，AC 更像是对 BF 的一层封装和修饰 【门面模式】
 *
 * 注意 —— ApplicationContext 继承了 ListableBeanFactory 和 HierarchicalBeanFactory
 * 上下文 继承 BF ？
 * 乍一看这是一个非常迷惑的设计，但是很快就能想到，因为 ApplicationContext 是 BF 的门面，仍然对外输出 getBean 的能力
 *
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader {

    ApplicationContext getParent();

}
