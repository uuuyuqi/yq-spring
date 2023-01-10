package com.yq.springframework.beans.factory.support;

import com.yq.springframework.beans.BeanInstantiationException;
import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.MutablePropertyValues;
import com.yq.springframework.beans.factory.config.BeanDefinition;
import com.yq.springframework.beans.factory.config.BeanReference;
import com.yq.springframework.test.Sample.beans.TestBean;
import com.yq.springframework.test.Sample.beans.TestBeanPlus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AbstractAutowireCapableBeanFactoryTest {

    AbstractAutowireCapableBeanFactory abstractAutowireCapableBF = new AbstractAutowireCapableBeanFactory() {
        //private final InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();
        @Override
        protected BeanDefinition getBeanDefinition(String beanName) throws BeansException {
            return null;
        }
    };

    /**
     * 测试 bean 实例的创建
     */
    @Test
    @DisplayName("BF createBeanInstance 使用默认构造器 正产,异常案例")
    public void createBeanInstance() {
        // 正常情况1
        String beanName = "tb";
        BeanDefinition bd = new BeanDefinition(TestBean.class);
        Object[] args = new Object[] {1001,"Zhang San"};

        TestBean bean = (TestBean)abstractAutowireCapableBF.createBean(beanName, bd, args);
        Assertions.assertEquals(1001+""+"Zhang San",bean.info());


        // 正常情况2
        String beanName2 = "tb2";
        BeanDefinition bd2 = new BeanDefinition(TestBean.class);
        Object[] args2 = new Object[] {new Integer(1002),"Zhang San2"};

        TestBean bean2 = (TestBean)abstractAutowireCapableBF.createBean(beanName2, bd2, args2);
        Assertions.assertEquals(1002+""+"Zhang San2",bean2.info());



        // 异常情况
        String beanName3 = "tb3";
        BeanDefinition bd3 = new BeanDefinition(TestBean.class);
        Object[] args3 = new Object[] {new Integer(1003),"Zhang San3","redundant param"};

        Assertions.assertThrows(BeanInstantiationException.class,()->{
            TestBean bean3 = (TestBean)abstractAutowireCapableBF.createBean(beanName3, bd3, args3);
        });
    }

    /**
     * Bean 实例创建 并且没有配置参数
     */
    @Test
    public void getBeanWithNoParam() {
        // 正常情况X args = null
        String beanNameX = "tb3";
        BeanDefinition bdX = new BeanDefinition(TestBean.class);
        Object[] argsX = null;

        TestBean beanX = (TestBean)abstractAutowireCapableBF.createBean(beanNameX, bdX, null);
        Assertions.assertEquals("9999999YOUR TEST WENT WRONG! PLEASE CHECK!",beanX.info());
    }


    /**
     * 测试从 lbf 注册 bean 开始，进行完整的 bean 实例的创建
     */
    @Test
    public void useBF_createBeanInstance() {
        DefaultListableBeanFactory lbf = new DefaultListableBeanFactory();
        BeanDefinition testBeanBD = new BeanDefinition(TestBean.class);
        lbf.registerBeanDefinition("tb",testBeanBD);

        TestBean testBean = (TestBean) lbf.getBean("tb",new Object[] {
            1001,"ZhangSan"
        });

        Assertions.assertEquals("1001ZhangSan",testBean.info());
    }


    /**
     * 测试 bean 的属性注入，并能支持对容器中其他 bean 的注入
     */
    @Test
    public void test_beanCreate_populate() {
        DefaultListableBeanFactory lbf = new DefaultListableBeanFactory();


        BeanDefinition worker_bd = new BeanDefinition(TestBean.class);
        MutablePropertyValues workerPvs = new MutablePropertyValues();
        workerPvs.addPropertyValue("id",1001);
        workerPvs.addPropertyValue("name","Zhangsan");
        worker_bd.setPropertyValues(workerPvs);

        BeanDefinition boss_bd = new BeanDefinition(TestBeanPlus.class);
        MutablePropertyValues bossPvs = new MutablePropertyValues();
        bossPvs.addPropertyValue("testBean",new BeanReference("worker"));

        lbf.registerBeanDefinition("boss",boss_bd);
        lbf.registerBeanDefinition("worker",worker_bd);

        //TestBeanPlus boss = (TestBeanPlus) lbf.getBean("boss");
        TestBean worker = (TestBean) lbf.getBean("worker");
        System.out.println(worker.info());

        //Assert.assertEquals("1001Zhangsan",boss.getInfo());

    }
}