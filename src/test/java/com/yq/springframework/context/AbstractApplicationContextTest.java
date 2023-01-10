package com.yq.springframework.context;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.MutablePropertyValues;
import com.yq.springframework.beans.factory.config.BeanDefinition;
import com.yq.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.yq.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.yq.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.yq.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import com.yq.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.yq.springframework.context.support.AbstractApplicationContext;
import com.yq.springframework.test.Sample.beans.TestBean;
import com.yq.springframework.test.Sample.context.refreshorder.TestBeanDefRegistryPP;
import com.yq.springframework.test.Sample.context.refreshorder.TestBeanDefRegistryPPGeneratorPP;
import com.yq.springframework.test.Sample.context.refreshstep1.TestBdrPP1;
import com.yq.springframework.test.Sample.context.refreshstep1.TestBdrPP2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbstractApplicationContextTest {

    AbstractApplicationContext abstractAC = new AbstractApplicationContext(){

        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();

        @Override
        protected void refreshBeanFactory() throws BeansException, IllegalStateException {
            if (bf == null)
                this.bf = new DefaultListableBeanFactory();
        }

        @Override
        public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
            return this.bf;
        }
    };


    /**
     *
     * 测试 refresh step 1
     *
     * 手工放一个 bdrPP 注册到 AC 中
     * 该 bdrPP 的功能是手工放一个 bean(BeanDef) 到 BF 中
     */
    @Test
    public void bdrPP_add_and_invoke() {

        StringBuilder execOrder = new StringBuilder(2);

        abstractAC.addBeanFactoryPostProcessor(new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
                BeanDefinition yqBD = new BeanDefinition(TestBean.class);
                MutablePropertyValues pvs = new MutablePropertyValues();
                pvs.addPropertyValue("id",123);
                pvs.addPropertyValue("name","wyq");
                yqBD.setPropertyValues(pvs);
                registry.registerBeanDefinition("wyq",yqBD);

                execOrder.append("1");
            }

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                execOrder.append("2");
            }
        });

        // refresh
        abstractAC.refresh();

        TestBean wyq = (TestBean) abstractAC.getBean("wyq");

        Assertions.assertEquals("123wyq",wyq.info());
        // 测试执行顺序
        // bdrPP 理应在 bfPP 之前, 结果应该是 12, 而非 21 或者其他
        Assertions.assertEquals("12",execOrder.toString());
    }

    /**
     * 测试 refresh step 1
     *
     * 测试多个 BDRPP bean 形式配置到 AC, 判断 PP 执行结果
     * 每个 BDRPP 都是 放一个 bean 到 BF(bdr) 中
     */
    @Test
    public void bdrPP_add_like_beans() {
        BeanDefinitionRegistry bf = (BeanDefinitionRegistry) abstractAC.getBeanFactory();

        // 每个 BDRP 都是往 spring 容器中放了一个 bean
        bf.registerBeanDefinition("bdrPP1", new BeanDefinition(TestBdrPP1.class));
        bf.registerBeanDefinition("bdrPP2", new BeanDefinition(TestBdrPP2.class));

        abstractAC.refresh();

        TestBean bean1 = (TestBean) abstractAC.getBean(TestBdrPP1.INFO);
        TestBean bean2 = (TestBean) abstractAC.getBean(TestBdrPP2.INFO);

        // 测试 BDRPP 放入的 bean 是否存在
        Assertions.assertEquals(TestBdrPP1.INFO,bean1.info());
        Assertions.assertEquals(TestBdrPP2.INFO,bean2.info());

    }

    /**
     * 测试多个 BDRPP 、 BFPP 共存情况下的执行, 判断 BDRPP、BFPP 执行顺序是否满足
     */
    @Test
    public void t3() {
        StringBuilder execOrder = new StringBuilder(20);

        abstractAC.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                execOrder.append("5");
                System.out.println("bfPP manually append");
            }
        });

        abstractAC.addBeanFactoryPostProcessor(new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
                execOrder.append("1");
                System.out.println("bdrPP manually append");
            }

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                execOrder.append("4");
                System.out.println("bdrPP-bfPP manually append");
            }
        });

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) abstractAC.getBeanFactory();
        registry.registerBeanDefinition("bdrPP_bean", new BeanDefinition(TestBeanDefRegistryPP.class));
        registry.registerBeanDefinition("bdrPP_Generator", new BeanDefinition(TestBeanDefRegistryPPGeneratorPP.class));


        abstractAC.refresh();

        Assertions.assertEquals("123456",execOrder.toString());

    }


    // TODO 测试多个 BDRPP 放一个新的 BDRPP, 看看由 BDRPP 产生的 BDRPP 是否顺序执行
    @Test
    public void t5() {
    }

    // TODO 把所有是 BPP 的 bean 都注册到了 BF 中
    @Test
    public void t6() {
    }


}