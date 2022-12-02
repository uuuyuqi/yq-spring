package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.factory.config.BeanDefinition;
import com.yq.springframework.test.Sample.beans.TestBean;
import org.junit.Assert;
import org.junit.Test;


public class DefaultListableBeanFactoryTest {

    private final DefaultListableBeanFactory lbf = new DefaultListableBeanFactory();

    /**
     * 测试单例池放入、取出
     */
    @Test
    public void testSingleton() {
        TestBean testBean = new TestBean(1000,"someTestBean");
        lbf.registerSingleton("tb",testBean);

        Assert.assertSame("getSingleton",testBean, lbf.getSingleton("tb"));
    }

    /**
     * 测试 BD 放入、取出
     */
    @Test
    public void testBeanDefinitionRegisterAndGet() {
        BeanDefinition bd = new BeanDefinition(TestBean.class);
        lbf.registerBeanDefinition("tb",bd);

        // 测试正常 bd 获取
        Assert.assertSame("registerBeanDefinition  && getBeanDefinition",lbf.getBeanDefinition("tb"),bd);

        // 测试获取不到 bd
        try {
            lbf.getBeanDefinition("Jack");
        }catch (Exception e){
            Assert.assertEquals("No bean named '" + "Jack" + "' available", e.getMessage());
        }

    }

    /**
     * 测试 getBean createBean
     */
    @Test
    public void testGetBeanAndCreateBean(){
        BeanDefinition bd = new BeanDefinition(TestBean.class);

        lbf.registerBeanDefinition("tb",bd);

        TestBean tb = (TestBean) lbf.getBean("tb");

        Assert.assertEquals("TestBean I am",tb.info());
    }
}