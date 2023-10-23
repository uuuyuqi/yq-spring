package com.yq.springframework.context.support;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.yq.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.yq.springframework.context.ApplicationContext;
import com.yq.springframework.context.ApplicationContextException;

import java.io.IOException;

/**
 *
 * 该类用来创建 BF
 *
 * 该类是对 AbstractApplicationContext 的重要扩展，提供了 2 大非常重要的能力：
 * - 首次在字段中定义了 beanFactory，终于使得 context 持有了 beanFactory
 * - 提供了 BF 创建、配置的模板(配置的流程中就包含了 BD 的加载过程!)
 *
 * 实际上在 spring 源码中，该类表示可以反复刷新的 spring 上下文，所以在源码中支持反复刷新
 * 每次刷新都会重新创建 BF
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{

    /**
     * 首次在 spring 上下文中持有 BF !
     */
    private DefaultListableBeanFactory beanFactory;

    public AbstractRefreshableApplicationContext() {
    }
    public AbstractRefreshableApplicationContext(ApplicationContext parent) {
        super(parent);
    }


    /**
     * 非常核心的模板方法
     * 提供了 创建和配置 BF 的模板
     * @throws BeansException ex
     */
    @Override
    protected final void refreshBeanFactory() throws BeansException {
        try {
            DefaultListableBeanFactory beanFactory = createBeanFactory();
            // 目前定制化只有2个地方能配置：
            // - 是否开启 bean 的覆盖
            // - 是否开启 循环依赖
            // 加载 BD ，这个地方包括了创建 BDReader、 BDScanner
            loadBeanDefinitions(beanFactory);
            this.beanFactory = beanFactory;
        }
        catch (IOException ex) {
            throw new ApplicationContextException("I/O error parsing bean definition source for ApplicationContext", ex);
        }
    }

    /**
     * 模板流程 1 —— 创建 BF
     * @return
     */
    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    /**
     * 模板流程 2 —— 加载 BD 资源!
     *
     * 这个地方也正是模板方法，具体实现是下沉到子类来做的
     *
     * @param beanFactory 将加载后的 BD 资源放入该 bf
     * @throws BeansException beans ex
     * @throws IOException io ex
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
            throws BeansException, IOException;




    public final ConfigurableListableBeanFactory getBeanFactory() {
        DefaultListableBeanFactory beanFactory = this.beanFactory;
        if (beanFactory == null) {
            throw new IllegalStateException("BeanFactory not initialized or already closed - " +
                    "call 'refresh' before accessing beans via the ApplicationContext");
        }
        return beanFactory;
    }
}
