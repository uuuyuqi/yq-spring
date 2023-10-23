package com.yq.springframework.context.support;

import com.yq.springframework.beans.BeansException;
import com.yq.springframework.context.ApplicationContext;

/**
 * 加载 classpath 路径下的 xml 配置文件
 *
 * 该类是提供给用户的最终的实用类，有两大功能：
 * 1.根据文件地址加载文件
 *
 *
 * 注：该类中大部分内容都是各式各样的构造器
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{


    public ClassPathXmlApplicationContext() {
    }

    public ClassPathXmlApplicationContext(ApplicationContext parent) {
        super(parent);
    }

    //=================================
    // 使用 configLocation 创建 context
    //=================================
    public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
        this(new String[] {configLocation}, true, null);
    }

    public ClassPathXmlApplicationContext(String... configLocations) throws BeansException {
        this(configLocations, true, null);
    }

    public ClassPathXmlApplicationContext(String[] configLocations, ApplicationContext parent)
            throws BeansException {

        this(configLocations, true, parent);
    }

    public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh) throws BeansException {
        this(configLocations, refresh, null);
    }

    /**
     * 根据配置文件，创建 spring 容器
     * 其中 refresh 参数表示是否立刻刷新 spring 上下文
     *
     * @param configLocations 配置文件地址
     * @param refresh 是否立刻刷新上下文
     * @param parent 父 context
     * @throws BeansException 创建上下文失败则抛出
     */
    public ClassPathXmlApplicationContext(
            String[] configLocations, boolean refresh, ApplicationContext parent)
            throws BeansException {

        super(parent);
        setConfigLocations(configLocations);

        // 关键! 说明在构造器里面，设置完配置文件之后，就进行刷新!
        if (refresh) {
            refresh();
        }
    }

}
