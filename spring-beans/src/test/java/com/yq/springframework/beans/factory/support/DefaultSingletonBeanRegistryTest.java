package com.yq.springframework.beans.factory.support;


import com.yq.springframework.test.Sample.beans.TestBean;
import org.junit.Assert;
import org.junit.Test;


public class DefaultSingletonBeanRegistryTest {

    DefaultSingletonBeanRegistry singletonRegistry = new DefaultSingletonBeanRegistry();

    @Test
    public void testSingletonRegisterAndGet(){
        TestBean testBean = new TestBean(1000,"someTestBean");

        singletonRegistry.registerSingleton("tb",testBean);

        Assert.assertSame(testBean,singletonRegistry.getSingleton("tb"));
    }

}