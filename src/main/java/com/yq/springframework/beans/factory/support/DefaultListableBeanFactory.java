package com.yq.springframework.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import com.yq.springframework.beans.BeansException;
import com.yq.springframework.beans.factory.BeanFactory;
import com.yq.springframework.beans.factory.NoSuchBeanDefinitionException;
import com.yq.springframework.beans.factory.config.BeanDefinition;
import com.yq.springframework.beans.factory.config.BeanPostProcessor;
import com.yq.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.yq.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 永远的主角！
 *
 * <p>是狭义上 spring 容器（虽然单例池更合适被认为是狭义上的IoC容器）的默认实现
 * 具备了祖先继承下来的 2 大核心功能：
 * <p>1. 来自祖先 BF 接口的 —— getBean 的功能
 * <p>2. 来自祖先 SingletonBeanRegistry 的单例 bean 注册功能（以及维护功能）
 */
public class DefaultListableBeanFactory
        extends AbstractAutowireCapableBeanFactory
        implements ConfigurableListableBeanFactory,  BeanDefinitionRegistry{

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);


    public DefaultListableBeanFactory() {
    }

    /**
     * 构造时可以将父工厂传入
     * @param parentBeanFactory 父工厂
     */
    public DefaultListableBeanFactory(BeanFactory parentBeanFactory) {
        super(parentBeanFactory);
    }

    /**
     * 由于继承了 BF，而 BF 需要 getBD的能力(因为 BF 在 getBean 获取不到的情况下，需要创建 Bean，这个时候就用到了 getBD 方法)
     * 而 getBD 是 BDRegistry 封装的能力
     * @param beanName BD 的 beanName
     * @return BD
     * @throws NoSuchBeanDefinitionException bean不存在
     */
    @Override
    protected BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException(beanName);
        }
        return beanDefinition;
    }

    /**
     * 此外，DLBF 还应该具备注册 BD 的能力
     * @param beanName BD 的 beanName
     * @param beanDefinition BD
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }

    /**
     * 获取当前 holder 中 BD 的个数
     * @return BD 的个数
     */
    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    /**
     * 查询当前 BD Map, 是否含有某 name 的 BD
     * @param beanName beanName
     * @return 是否含有某 name 的 BD
     */
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    /**
     * 返回指定类型的所有实例
     *
     * @param type bean 类型
     * @return beanName ==> bean (map)
     * @throws BeansException ex
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> result = new HashMap<>();

        String[] beanNames = getBeanNamesForType(type);

        for (String beanName : beanNames) {
            Object beanInstance = getBean(beanName);
            result.put(beanName,(T)beanInstance);
        }

        return result;
    }

    /**
     * 返回定义的所有 bean 的名称
     *
     * @return 所有 bean 的名称
     */
    @Override
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[0]);
    }


    /**
     * 按照类型 tyoe 来查找 bean
     *
     * 本方法在 spring 上下文的 refresh 方法里的 invokeBFPP 这一步中会经常调用！
     * @param type bean 类型
     * @return bean names
     */
    @Override
    public String[] getBeanNamesForType(Class<?> type) {

        ArrayList<String> beanNames = new ArrayList<>();

        this.beanDefinitionMap.forEach((beanName,bd)->{
            // spring 源码此处使用了 isTypeMatch() 方法，考虑了更多情况
            // 这里的 isAssignableFrom() 方法对于子类和接口实现类皆可使用
            if (type.isAssignableFrom(bd.getBeanClass())) {
                beanNames.add(beanName);
            }
        });
        return beanNames.toArray(new String[0]);
    }


    /**
     * 加载所有非惰性加载(non lazy-init)的 bean
     *
     * @throws BeansException ex
     */
    @Override
    public void preInstantiateSingletons() throws BeansException {
        List<String> beanNames = new ArrayList<>();
        this.beanDefinitionMap.forEach((beanName, bd) -> beanNames.add(beanName));

        for (String beanName : beanNames) {
            // TODO 对 factoryBean 的处理
            getBean(beanName);
        }

    }
}
