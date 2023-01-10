package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.factory.NoSuchBeanDefinitionException;
import com.yq.springframework.beans.factory.config.BeanDefinition;
import com.yq.springframework.test.Sample.beans.TestBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class DefaultListableBeanFactoryTest {

    private final DefaultListableBeanFactory lbf = new DefaultListableBeanFactory();

    /**
     * 测试单例池放入、取出
     */
    @Test
    public void testSingleton() {
        TestBean testBean = new TestBean(1000,"someTestBean");
        lbf.registerSingleton("tb",testBean);

        Assertions.assertSame(testBean, lbf.getSingleton("tb"));
    }

    /**
     * 测试 BD 放入、取出
     */
    @Test
    public void testBeanDefinitionRegisterAndGet() {
        BeanDefinition bd = new BeanDefinition(TestBean.class);
        lbf.registerBeanDefinition("tb",bd);

        // 测试正常 bd 获取
        Assertions.assertSame(lbf.getBeanDefinition("tb"),bd);

        // 测试获取不到 bd
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,()->{
            lbf.getBeanDefinition("Jack");
        });
    }

    /**
     * 测试 getBean createBean
     */
    @Test
    public void testGetBeanAndCreateBean(){
        BeanDefinition bd = new BeanDefinition(TestBean.class);

        lbf.registerBeanDefinition("tb",bd);

        TestBean tb = (TestBean) lbf.getBean("tb");

        Assertions.assertEquals("TestBean I am",tb.speakSelf());
    }
}