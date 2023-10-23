package com.yq.springframework.beans.factory.xml;

import com.yq.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.yq.springframework.test.Sample.beans.TestBeanPlus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class XmlBeanDefinitionReaderTest {

    DefaultListableBeanFactory lbf = new DefaultListableBeanFactory();
    XmlBeanDefinitionReader bdReader = new XmlBeanDefinitionReader(lbf);

    @Test
    public void loadBeanDefinitions_filePath() {
        String configFile = "classpath:spring-context.xml";
        bdReader.loadBeanDefinitions(configFile);

        // 按照 location(String) 的形式加载 resource
        TestBeanPlus beanPlus = (TestBeanPlus) lbf.getBean("tbp");
        String info = beanPlus.getTestBean().info();

        Assertions.assertEquals("1000ZDK", info);
    }

    /**
     * 测试 bean 重复注册
     */
    @Test
    public void loadBeanDefinitions_urlPath() {
        String configUrl = "http://localhost/getXml";
        bdReader.loadBeanDefinitions(configUrl);

        // 按照 location(String) 的形式加载 resource
        TestBeanPlus beanPlus = (TestBeanPlus) lbf.getBean("tbp");
        String info = beanPlus.getTestBean().info();

        Assertions.assertEquals("1000ZDK", info);
    }


    /**
     * 测试 bean 重复注册
     */
    @Test
    public void loadBeanDefinitions_duplicated() {
        String configFile = "classpath:spring-context-duplicatedBean.xml";
        try {
            bdReader.loadBeanDefinitions(configFile);
        }catch (Exception e){
            Assertions.assertEquals("已存在该名称[tbp]的 bean 元信息! bean 名称不允许重复!",
                    e.getMessage());
        }
    }
}