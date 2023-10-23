package com.yq.springframework.context.support;


import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.yq.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.yq.springframework.context.ApplicationContext;
import com.yq.springframework.core.io.Resource;

import java.io.IOException;

/**
 * XML 方式配置出来的 ApplicationContext
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableConfigApplicationContext{


    public AbstractXmlApplicationContext() {
    }

    public AbstractXmlApplicationContext(ApplicationContext parent) {
        super(parent);
    }


    /**
     * 读取 XML 配置
     *
     * @param beanFactory 将加载后的 BD 资源放入该 bf
     * @throws BeansException beans ex
     * @throws IOException    io ex
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {

        // 发生了不友好的依赖关系 —— BeanDefinitionReader， 直接在方法内部进行依赖
        // 为什么这个地方不用 成员变量的形式来依赖 XmlBeanDefinitionReader? 这个地方是有学问的
        // 因为在源码设计中，上层的设计 AbstractRefreshableAC 是支持多次 refresh 的。而 BDReader 本身并非一个工具对象，而是一个有状态(bdr、env、loader属性)的对象
        // 应该在每次 loadBD 时，创建一个新的 BDReader 来使用！
        XmlBeanDefinitionReader xmlBdReader = new XmlBeanDefinitionReader(beanFactory);
        xmlBdReader.setResourceLoader(this);


        /*// 加载不同路径下的资源
        Resource[] configResources = getConfigResources();
        if (configResources != null) {
            xmlBdReader.loadBeanDefinitions(configResources);
        }*/
        // 加载配置好的路径下的文件
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            xmlBdReader.loadBeanDefinitions(configLocations);
        }
    }



    /**
     * 获取资源
     * 不同的子类实现，获取的来源和方式不一样
     * @return resource[]
     */
    protected Resource[] getConfigResources() {
        return null;
    }
}
