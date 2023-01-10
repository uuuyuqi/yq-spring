package com.yq.springframework.beans.factory.support;


import com.yq.springframework.test.Sample.beans.TestBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class DefaultSingletonBeanRegistryTest {

    DefaultSingletonBeanRegistry singletonRegistry = new DefaultSingletonBeanRegistry();

    /**
     * 单例的注册和获取
     */
    @Test
    public void testSingletonRegisterAndGet(){
        TestBean testBean = new TestBean(1000,"someTestBean");

        singletonRegistry.registerSingleton("tb",testBean);

        Assertions.assertSame(testBean,singletonRegistry.getSingleton("tb"));
    }

}