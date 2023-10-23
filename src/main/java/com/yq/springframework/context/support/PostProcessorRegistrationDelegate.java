package com.yq.springframework.context.support;

import com.yq.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.yq.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.yq.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.yq.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.*;

/**
 * PostProcessor 处理器，可以处理 BeanFactoryPostProcessor 以及 BeanPostProcessor
 *
 * 属实委派模式，充分将 spring context 与底层修饰 BF 的操作解耦、将 Bean Factory 与具体修饰 Bean 的操作解耦
 */
final public class PostProcessorRegistrationDelegate {
    private PostProcessorRegistrationDelegate() {
    }

    /**
     * 执行 BeanDefinitionRegistryPostProcessor 和 BeanFactoryPostProcessor
     * 执行过程会考虑前后的顺序及依赖关系
     * @param beanFactory bf
     * @param ppAddedDirectlyFromContext bfpp added manually
     */
    public static void invokeBeanFactoryPostProcessors(
            ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> ppAddedDirectlyFromContext ){

        // 手工通过调用 AC 的 addBeanFactoryPostProcessors 方法添加进来的 BFPP
        // 在 spring 中约定，直接通过手工的方式 add 进来的，优先级最高！最先被执行！
        // 整体优先级顺序如下：(不考虑priordered ordered)
        // bdrPP(手工add) > bdrPP(bean) > bdrPP(由bdrPP产生) > bdrPP(手工add)bfPP > bfPP(手工add) > bfPP(bean)

        // 在处理 bdrPP 过程中，会发现一些 bfPP，而且有些 bdrPP 本身也是 bfPP
        // 这些 pp 需要手工标记一下，防止在 part 2 处重复执行
        // 归根结底，这么复杂的原因，个人理解根源纯粹是由于 bdrPP 继承自 bfPP，搞得这段代码非常复杂、不伦不类
        // 就应该搞两个接口！ 这么做个人理解已经违背了【单一职责】原则！
        Set<String> processedBeans = new HashSet<>();

        //////////////////////////////////////////////////////////////////////////////
        //                          part 1 —— invoke bdRegistryPP
        //////////////////////////////////////////////////////////////////////////////

        // 如果传进来的 BF 也是 BD Registry(实则必然), 才需要开展 part 1 的操作
        // 绝大多数情况下，这里的 beanFactory 就是 DefaultListableBF !
        if (beanFactory instanceof BeanDefinitionRegistry) {
            BeanDefinitionRegistry bdRegistry = (BeanDefinitionRegistry) beanFactory;

            //=============================================================
            // (1) 处理 addBeanFactoryPostProcessor 方式添加进来的 bdrPP
            //=============================================================

            // 将过程中遇到的 bdrPP 缓存起来，方便后面进行操作(4)
            List<BeanDefinitionRegistryPostProcessor> bdrPPs = new ArrayList<>();
            // 将过程中遇到的 bfPP 缓存起来，方便后面进行操作(5)
            List<BeanFactoryPostProcessor> bfPPs = new ArrayList<>();

            for (BeanFactoryPostProcessor processor : ppAddedDirectlyFromContext) {
                if (processor instanceof BeanDefinitionRegistryPostProcessor) {
                    BeanDefinitionRegistryPostProcessor bdRegistryPP = (BeanDefinitionRegistryPostProcessor) processor;
                    // 直接调用!
                    bdRegistryPP.postProcessBeanDefinitionRegistry(bdRegistry);
                    // 缓存起来，等 bfProcess 阶段，执行这些 registryPP 对 bfProcess 实现
                    // (确实可能有 registryPP 不仅 process registry, 也 process bf, 毕竟 bdrPP extends bfPP)
                    bdrPPs.add(bdRegistryPP);
                } else {
                    // 非 bdrPP ====> 普通的 bfPP
                    bfPPs.add(processor);
                }
            }

            //=============================================================
            // (2) 处理 spring bean 形式配置的 bdrPP
            //=============================================================
            List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

            // 这里有个细节，调用 getBeanNamesForType 获取 beanName
            // 而不直接 getBeansOfType 直接获取 bean，根源就在于此处不应该直接 getBean！ 应该先获取 beanName
            // 然后按照优先级顺序，依次来 getBean，这样保证后 getBean 的 processor bean，可以被先 getBean 的 processor bean处理
            String[] bdrPPNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class);
            for (String bdrPPName : bdrPPNames) {
                BeanDefinitionRegistryPostProcessor bdrPP = (BeanDefinitionRegistryPostProcessor) beanFactory.getBean(bdrPPName);
                currentRegistryProcessors.add(bdrPP);
                processedBeans.add(bdrPPName); // 最后标记为已处理
            }

            bdrPPs.addAll(currentRegistryProcessors);
            // invoke
            invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors,bdRegistry);
            currentRegistryProcessors.clear();

            //=============================================================
            // (3) 调用由前面的 bdrPP 产生的 bdrPP
            //=============================================================
            boolean stillComeUpNew = true;
            while (stillComeUpNew) {

                stillComeUpNew = false; // 先假设当前不会有新的 bdrPP 产生了

                String[] currentPPNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class);
                for (String currentPPName : currentPPNames) {
                    // 找到新产生的 (之前没处理的)
                    if (!processedBeans.contains(currentPPName)) {
                        BeanDefinitionRegistryPostProcessor bdrPP = (BeanDefinitionRegistryPostProcessor) beanFactory.getBean(currentPPName);
                        currentRegistryProcessors.add(bdrPP);
                        processedBeans.add(currentPPName);

                        // 发现了新 bdrPP，等会还需要再坚持一下有没有新的 bdrPP 产生！
                        stillComeUpNew = true;
                    }
                }

                // 将本轮循环发现的 bdrpp 全部添加到 bdrPPs 中
                bdrPPs.addAll(currentRegistryProcessors);
                invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors,bdRegistry);
                currentRegistryProcessors.clear();
            }


            //=============================================================
            // (4) 统一回调这些 bdRegistryPP 对于 bfPP 的接口实现(实际上不一定有)
            //=============================================================
            invokeBeanFactoryPostProcessors(bdrPPs,beanFactory);

            //=============================================================
            // (5) 补充回调一下通过 addBeanFactoryPostProcessor 方式添加进来的 bfPP
            //=============================================================
            invokeBeanFactoryPostProcessors(bfPPs, beanFactory);

        } else {
            invokeBeanFactoryPostProcessors(ppAddedDirectlyFromContext,beanFactory);
        }


        //////////////////////////////////////////////////////////////////////////////
        //                         part 2 —— invoke beanFactoryPP
        //////////////////////////////////////////////////////////////////////////////
        List<BeanFactoryPostProcessor> currentBfProcessors = new ArrayList<>();
        String[] bfPPNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class);
        for (String bfPPName : bfPPNames) {
            // 对 BeanFactoryPostProcessor 进行 getBeanNamesForType, 会把 step1 中的 bdrPP也给 get 出来
            // (由于 BeanDefinitionRegistryPostProcessor 是 BeanFactoryPostProcessor 的子接口，所以会把 part 1 中的接口查出来)
            if (!processedBeans.contains(bfPPName)) {
                BeanFactoryPostProcessor bfPP = (BeanFactoryPostProcessor) beanFactory.getBean(bfPPName);
                currentBfProcessors.add(bfPP);
            }
        }
        invokeBeanFactoryPostProcessors(currentBfProcessors,beanFactory);
    }

    /**
     * 单纯调用 bdrPP
     */
    private static void invokeBeanDefinitionRegistryPostProcessors(
            Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {

        for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanDefinitionRegistry(registry);
        }
    }


    /**
     * 单纯调用 bfPP
     */
    private static void invokeBeanFactoryPostProcessors(
            Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

        for (BeanFactoryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanFactory(beanFactory);
        }
    }
}
