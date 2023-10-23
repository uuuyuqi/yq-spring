package com.yq.springframework.context.support;

import cn.hutool.core.lang.Assert;
import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.BeanFactory;
import com.yq.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.yq.springframework.beans.factory.config.BeanPostProcessor;
import com.yq.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.yq.springframework.context.ApplicationContext;
import com.yq.springframework.context.ConfigurableApplicationContext;
import com.yq.springframework.core.io.DefaultResourceLoader;

import java.util.*;


/**
 * spring 容器的抽象实现
 *
 * <p>
 * 重点：
 *     1.主要是创建和配置 BeanFactory
 *     2.重点是为 refresh 方法提供了一套模板实现！
 *     3.具备加载资源的能力
 * <p>
 * 注意本抽象类不持有 BF， 后续子类才会持有 BF !
 *
 */
public abstract class AbstractApplicationContext
        extends DefaultResourceLoader
        implements ConfigurableApplicationContext {

    private ApplicationContext parent;

    /**
     * beanFactoryPostProcessors
     */
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    public AbstractApplicationContext() {
    }

    public AbstractApplicationContext( ApplicationContext parent) {
        setParent(parent);
    }


    public ApplicationContext getParent() {
        return this.parent;
    }

    public void setParent(ApplicationContext parent) {
        this.parent = parent;
    }


    /**
     * ★★★★★
     * 刷新 spring 上下文的 【模板方法】, 主要工作其实就是:
     * 1.创建 BF
     * 2.配置 BF
     * 3.获取 Bean
     *  ★★★★★
     * @throws BeansException ex
     */
    @Override
    public void refresh() throws BeansException {
        // 1. 创建、配置 BF
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // 2. 调用 BeanFactoryPostProcessor
        // 注意，bean 就在此时扫描出来的 (springboot 启动类也会在此时被扫描，同时项目中的注解 Bean 也会在此时被注册)
        invokeBeanFactoryPostProcessors(beanFactory);

        // 3. 注册所有的 BeanPostProcessor
        registerBeanPostProcessors(beanFactory);

        // 4. 对所有非 lazy-init 的 bean 进行 getBean
        finishBeanFactoryInitialization(beanFactory);

    }


    //==================================
    // step1   obtainFreshBeanFactory()
    //==================================

    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        refreshBeanFactory();
        return getBeanFactory();
    }

    /**
     * 创建 BF， 对 BF 进行配置
     * 在 spring 源码中，这个地方主要做3个事情：
     * (1)创建 BF —— 其实就是 new 了一个 DefaultListableBF
     * (2)配置 BF —— 目前只有2个配置项：是否开启循环依赖？是否开启 bean 定义覆盖？
     * (3)加载 BD —— 会直接 new 一个 BD Reader、BD Scanner
     * <p>
     * <p>
     * 对子类的约束：该过程会在 AC 的 fresh 方法中调用，且在任何初始化步骤之前调用！
     *
     * @throws BeansException        加载 BF 失败
     * @throws IllegalStateException 不支持反复加载，只允许加载一次
     */
    protected abstract void refreshBeanFactory() throws BeansException, IllegalStateException;

    /**
     * 返回 BF
     *
     * @return 当前在处理的 BF
     * @throws IllegalStateException AC 中没有持有 BF 则会抛出该异常
     */
    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;


    //==================================
    // step2   invokeBeanFactoryPostProcessors(beanFactory)
    //==================================
    /**
     * 调用所有的 beanFactoryPostProcessor
     * <p>
     * 注意：
     * 1.在 spring 源码中，增加了一些优先级的概念和实现：(1)PriorityOrdered接口实现类、(2)Ordered、(3)剩余的
     * 实现上述接口的 PP 按照上述顺序执行
     * <p>
     * 2.在spring源码中，对于 BFPP 的调用采用了委派模式，直接将 BFPP 的调用策略委派给 PostProcessorRegistrationDelegate
     *
     * @param beanFactory 待处理的 beanFactory
     */
    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
       PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory,getBeanFactoryPostProcessors());
    }


    //==================================
    // step3   registerBeanPostProcessors(beanFactory)
    //==================================

    /**
     * 注册所有的 beanPostProcessor
     *
     * 在 spring 中，beanPostProcessor 和 beanFactoryPostProcessor 除了功能和调用时间节点不一样之外，还有个重要区别：
     * - beanFactoryPostProcessor 既可以通过 bean 的方式注册，也可以手工 add 到 spring 上下文中
     * - beanPostProcessor 只允许通过 bean 的方式注册到 beanFactory 中
     * @param beanFactory 待处理的 beanFactory
     */
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> processors = beanFactory.getBeansOfType(BeanPostProcessor.class);
        processors.forEach(
                (processName,processor) -> beanFactory.addBeanPostProcessor(processor)
        );
    }


    //==================================
    // step4   finishBeanFactoryInitialization(beanFactory)
    //==================================

    /**
     * 加载所有非懒加载的 bean
     *
     * @param beanFactory 待处理的 beanFactory
     */
    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.preInstantiateSingletons();
    }


    //=========================================================================
    // implements ConfigurableApplicationContext
    //=========================================================================

    /**
     * 将 bfpp 添加进来
     *
     * @param postProcessor bfpp
     */
    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        Assert.notNull(postProcessor, "BeanFactoryPostProcessor must not be null");
        this.beanFactoryPostProcessors.add(postProcessor);
    }

    /**
     * 获取当前上下文持有的 BFPP
     *
     * @return BFPPs
     */
    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }



    //=========================================================================
    // implements BeanFactory
    //=========================================================================

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name,args);
    }

    //=========================================================================
    // implements HierarchicalBeanFactory
    //=========================================================================

    /**
     * 获取父工厂
     * 注意，虽然这里获取的父上下文，而非父 BF ！
     * @return 父工厂
     */
    @Override
    public BeanFactory getParentBeanFactory() {
        return getParent();
    }


    //=========================================================================
    // implements ListableBeanFactory
    //=========================================================================

    /**
     * 返回指定类型的所有实例
     *
     * @param type bean 类型
     * @return beanName ==> bean (map)
     * @throws BeansException ex
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    /**
     * 返回定义的所有 bean 的名称
     *
     * @return 所有 bean 的名称
     */
    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    /**
     * 按照类型 tyoe 来查找 bean
     *
     * @param type bean 类型
     * @return bean names
     */
    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }
}
