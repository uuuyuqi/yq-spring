package com.yq.springframework.context.support;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.yq.springframework.context.ApplicationContext;

/**
 * 该类抽象了 AC 需要配置文件的能力。并存储了 配置源。
 *
 * 个人觉得这一层有点鸡肋，因为对配置文件的处理完全可以直接放入到 XmlAC 中
 * 但是还是遵循 spring 中的设计，加了这一层，只不过在 spring 中多了一步对配置文件信息的处理
 */
public abstract class AbstractRefreshableConfigApplicationContext extends AbstractRefreshableApplicationContext{

    /**
     * 配置文件分隔符
     * 在 spring 源码中支持[,] [;] [\t] [\n]
     */
    public static String CONFIG_LOCATION_DELIMITER = ";";

    /**
     * 配置文件地址
     *
     * 在 spring 源码中，configLocations字段 和 子类中的 AbstractXmlApplicationContext 中的 configResources字段 很容易混淆！
     * 这个配置地址，一般是：通用且具体的配置文件的地址！
     *
     * 本类中：configLocations —— 更高通用的配置，表示 spring 上下文可以手工 set 具体的配置文件地址，
     * 子类中：configResources —— 更底层的配置，表示具体某种方式加载出来的资源配置（比如 classpath、或者是 fileSystem）
     *
     * 虽然二者所处层次不同，但是二者作用不是互相覆盖的关系，而是互相补充的关系，在 BDReader 读取 BD 时，这两个地方都会读取！
     */
    private String[] configLocations;

    public AbstractRefreshableConfigApplicationContext() {
    }

    public AbstractRefreshableConfigApplicationContext(ApplicationContext parent) {
        super(parent);
    }


    /**
     * 设置配置文件位置
     * @param location 配置文件位置
     */
    public void setConfigLocation(String location) {
        setConfigLocations(StrUtil.split(location,CONFIG_LOCATION_DELIMITER));
    }

    public void setConfigLocations(String... locations) {
        if (locations != null) {
            Assert.noNullElements(locations, "Config locations must not be null");
            this.configLocations = locations;
        }
        else {
            this.configLocations = null;
        }
    }

    public String[] getConfigLocations() {
        return configLocations;
    }


}
