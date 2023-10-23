package com.yq.springframework.beans.factory.config;

import com.yq.springframework.beans.BeansException;

/**
 * BPP —— spring 中最常用的扩展点！！！
 *
 * 不过令人匪夷所思的是，BPP 只提供了 2 个方法： ppBeforeInitialization、 ppAfterInitialization
 * 也就是说，只提供了【初始化前处理】、【初始化后处理】！但实际上，我们 spring 体系中，不仅仅在初始化前后有 postprocess 处理
 * 在 bdMerge、instantiate前后 等等地方，都有 postProcess 的扩展机制。
 * 但是这些地方都是通过继承 BPP 的方式，自定义处理方法！
 *
 * 也正因如此，spring 的开发人员，才把这两个核心行为，都设置成了 default, 也预示了这两个接口并不是 必须实现的！
 *
 * 所以像 instantiate 的 post 处理器接口在继承了 BeanPostProcessor 之后，自己又定义了一些 ppBeforeInstantiation、ppAfterInstantiation等
 *
 * 所以这个 BeanPostProcessor 的设计【并不是特别优雅】，spring 设计这个处理器的目的，
 * 个人觉得还是想定义一套行为，这个行为就是在 bean 实例化、初始化等地方可以人为干预，自定义一些处理措施，但是也不好去让这些行为变得通用
 *
 */
public interface BeanPostProcessor {

    /**
     * 初始化前处理
     * 处理过程中，可以人为修饰、甚至是替换被处理的 bean
     * @param bean 待处理的 bean (此时已经完成了实例化和属性注入)
     * @param beanName bean 名称
     * @return 被处理后的 bean, 值得注意的是！spring 中约定：这个地方返回 null 的话，后续的 BeanPostProcessor 处理器将不再进行处理！！！！！
     * @throws BeansException ex
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 初始化后处理
     * 处理过程中，可以人为修饰、甚至是替换被处理的 bean
     * 但是注意一点！如果是 factoryBean，这个地方会考虑：对 fbean 进行调用？ 对 fbean 的包装对象进行调用？ 对二者都调用？
     * @param bean 待处理的 bean (此时已经完成了实例化和属性注入)
     * @param beanName bean 名称
     * @return 被处理后的 bean, 值得注意的是！spring 中约定：这个地方返回 null 的话，后续的 BeanPostProcessor 处理器将不再进行处理！！！！！
     * @throws BeansException ex
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
