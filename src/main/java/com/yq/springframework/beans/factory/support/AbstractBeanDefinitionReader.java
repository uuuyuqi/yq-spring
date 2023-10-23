package com.yq.springframework.beans.factory.support;

import cn.hutool.core.lang.Assert;
import com.yq.springframework.beans.factory.BeanDefinitionStoreException;
import com.yq.springframework.core.io.DefaultResourceLoader;
import com.yq.springframework.core.io.Resource;
import com.yq.springframework.core.io.ResourceLoader;

/**
 * BD Reader 的抽象实现类
 * 其实 spring 在设计层面上，酷爱使用:
 *     顶级接口(声明行为) —— 抽象实现(基本模板方法) —— 个性化/具体实现(进行具体的、不同方面的功能实现)
 * 比如:
 *     BF -- AbstractBF -- AutowiredCapableBF -- DefaultListableBF
 *     SingletonRegistry -- DefaultSingletonRegistry -- AbstractBeanFactory
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    /**
     * 注意，BD Holder 是 final 的哦！ only one
     */
    private final BeanDefinitionRegistry registry;

    /**
     *  一般情况下都是 DefaultResourceLoader 反正这一个基本直接通吃所有类型的 resource
     */
    private ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        this.registry = registry;

        if (this.registry instanceof ResourceLoader) {
            this.resourceLoader = (ResourceLoader) this.registry;
        }
        else {
            this.resourceLoader = new DefaultResourceLoader();
        }
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return this.registry;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }


    @Override
    public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
        // spring 源码中，这个地方支持模式匹配，返回结果是一个 Resource[]
        // 个人理解就是 location 中含有*
        Resource resource = getResourceLoader().getResource(location);
        int count = loadBeanDefinitions(resource);
        return count;
    }

    /**
     * spring 酷爱这种设计，在接口的抽象实现类中，搞个一个朴实无华的模板方法，解决了可变参数输入的问题
     * @param resources resource array
     * @return BD loaded num
     * @throws BeanDefinitionStoreException BD store ex
     */
    @Override
    public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
        Assert.notNull(resources, "Resource array must not be null");

        // 这个地方有待商榷，源码中是直接 int count = 0;
        // 但是这个 loadBeanDefinitions 确实还有有可能反复调用的
        int count = getRegistry().getBeanDefinitionCount();
        for (Resource resource : resources) {
            count += loadBeanDefinitions(resource);
        }
        return count;
    }

    /**
     * 同上
     */
    @Override
    public int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException {
        Assert.notNull(locations, "Location array must not be null");

        int count = getRegistry().getBeanDefinitionCount();
        for (String location : locations) {
            count += loadBeanDefinitions(location);
        }
        return count;
    }
}
