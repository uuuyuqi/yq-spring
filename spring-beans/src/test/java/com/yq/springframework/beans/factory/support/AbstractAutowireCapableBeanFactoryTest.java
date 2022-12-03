package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.config.BeanDefinition;
import com.yq.springframework.test.Sample.beans.TestBean;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractAutowireCapableBeanFactoryTest {

    AbstractAutowireCapableBeanFactory abstractAutowireCapableBF = new AbstractAutowireCapableBeanFactory() {
        @Override
        protected BeanDefinition getBeanDefinition(String beanName) throws BeansException {
            return null;
        }
    };

    @Test
    public void createBeanInstance() {
        // 正常情况1
        String beanName = "tb";
        BeanDefinition bd = new BeanDefinition(TestBean.class);
        Object[] args = new Object[] {1001,"Zhang San"};

        TestBean bean = (TestBean)abstractAutowireCapableBF.createBean(beanName, bd, args);
        Assert.assertEquals(1001+""+"Zhang San",bean.info());


        // 正常情况2
        String beanName2 = "tb2";
        BeanDefinition bd2 = new BeanDefinition(TestBean.class);
        Object[] args2 = new Object[] {new Integer(1002),"Zhang San2"};

        TestBean bean2 = (TestBean)abstractAutowireCapableBF.createBean(beanName2, bd2, args2);
        Assert.assertEquals(1002+""+"Zhang San2",bean2.info());


        // 异常情况
        String beanName3 = "tb3";
        BeanDefinition bd3 = new BeanDefinition(TestBean.class);
        Object[] args3 = new Object[] {new Integer(1003),"Zhang San3","redundant param"};

        try{
            TestBean bean3 = (TestBean)abstractAutowireCapableBF.createBean(beanName3, bd3, args3);
        }catch (Exception e){
            Assert.assertEquals("Failed to instantiate [" + TestBean.class +"]: 无法根据提供的参数找到合适的构造器",e.getMessage());
        }

    }



    @Test
    public void useBF_createBeanInstance() {
        DefaultListableBeanFactory lbf = new DefaultListableBeanFactory();
        BeanDefinition testBeanBD = new BeanDefinition(TestBean.class);
        lbf.registerBeanDefinition("tb",testBeanBD);

        TestBean testBean = (TestBean) lbf.getBean("tb",new Object[] {
            1001,"ZhangSan"
        });

        Assert.assertEquals("1001ZhangSan",testBean.info());
    }
}