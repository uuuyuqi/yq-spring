package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * BeanDefinitionRegistryPostProcessor
 *
 * 可以在 BDRegistry 创建完毕后进行执行！
 *
 * 【注意】
 * 1.在 spring 5中，BDRegistryPostProcessor 会在所有的 BFPP 调用之前就执行，优先级非常高！
 * (可能也是考虑到 BDRegistryPostProcessor 会注册一些 BFPP)
 * 2.有些 BDRegistryPostProcessor 的实现类可能也实现了 BeanFactoryPostProcessor 中的接口（意思就是方法体内确实有要执行的内容）
 * 那么这些 BDRegistryPostProcessor 的实现类会缓存起来，等 registryProcess 接口方法实现完了后，统一实现 bfProcess 方法
 *
 * 【误区】
 * - 不是在所有 BD 注册完毕后，调用 BDRegistryPostProcessor 这类接口
 * - 而是在 BDRegistry 创建完毕后(一般情况下就是 DefaultListableBeanFactory )，发起调用
 *
 * 【作用】
 * - 可以手工添加、修改一些 BeanDefinition 的属性
 * - 可以添加一些 BFPP 来配置 BF
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

    /**
     * BDRegistry 准备完毕后开始调用
     */
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;
}
