package com.yq.springframework.beans.factory.support;


import com.yq.springframework.beans.factory.BeanDefinitionStoreException;
import com.yq.springframework.core.io.Resource;
import com.yq.springframework.core.io.ResourceLoader;

/**
 * BD 信息加载器  顶层接口
 * BD reader 主要就使用用来读取 BD，然后将其存入 BD holder (BeanDefinitionRegistry) 中：
 *      (1) 用 ResourceLoader 获取 可能存放 BD 的资源
 *      (2) 通过不同的方式(子类实现), 将这些资源中的 BD 读取出来
 *      (3) 将读取出来的 BD , 放入 BeanDefinitionRegistry
 *
 */
public interface BeanDefinitionReader {
    /**
     * get BD holder
     * I will put BD in your "registry"
     *
     *
     * 【前方高能!】
     * 请注意！ 在真正实践的过程中，这个 BD registry 就是我们的 BF， 我们的 spring 容器！
     * 因为:
     * <pre>{@code
     *     public abstract class AbstractBeanFactory implements BeanDefinitionRegistry
     * }</pre>
     * 所以！我们 BD Reader，借助 ResourceLoader 获取 Resource，再通过 Reader 自身的读取能力，将 BD 读出来，放入 spring 容器！
     * 所以，我们的 spring 容器，经过 BD Reader 努力之后，才能装备这些 BD 信息
     *
     * 【个人疑惑】
     * 问题来了，spring 的这种设计，明显是一个类(BF)依赖了另一个类(BD reader)才完成初始化(BD 的装配)！ 为什么不让 BF 持有 BDReader呢？
     * 将 BD 的读取，归化成 BF 的能力不好吗？ spring 这么设计，估计还是为了在某个地方留下扩展的口子？
     *
     * @return BD holder, always be BeanDefinitionRegistry in spring
     *
     * @see com.yq.springframework.beans.factory.support.AbstractBeanFactory
     */
    BeanDefinitionRegistry getRegistry();

    /**
     * get resource loader
     * I want to resolve the resource which may contain the BDs, so I need you
     * @return BD Resource Loader
     */
    ResourceLoader getResourceLoader();

    /**
     * 按单个 resource 加载 BD
     * 实际上，接口中其他的 BD 加载方法，最后都是委托本方法来加载的，比如：
     * resource[] 的加载，就是本方法外面加了个for循环；
     * location 的加载，就是通过 ResourceLoader，解析成 Resource，再用本方法去加载
     * @param resource resource containing the BDs
     * @return BD 的个数，一直觉得这个返回值非常诡异，这里强行返回多少有点奇怪...
     * @throws BeanDefinitionStoreException BD store ex
     */
    int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException;

    /**
     * 同上，按多个 resource 加载 BD (vararg param)
     */
    int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException;

    /**
     * 按照 location 来加载 BD
     * 这里在实现上有个细节：按照 location(String) 来加载 BD，实际上的行为是：
     *      (1) 按照 location 加载 resource
     *      (2) 将加载任务，委托给重载方法 loadBeanDefinitions(Resource resource) ，按照 resource 加载 BD
     * @param location 地址
     * @return BD nums resolved
     */
    int loadBeanDefinitions(String location) throws BeanDefinitionStoreException;

    /**
     * 同上，可变参数
     */
    int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException;
}
