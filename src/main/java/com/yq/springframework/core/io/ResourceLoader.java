package com.yq.springframework.core.io;

/**
 * Resource 资源加载的顶层接口，定义了资源加载行为
 * 个人觉得，这个所谓的 Resource，其实就是 —— BeanDefinitionResource !
 *
 * 值得注意的是，ResourceLoader 只是 Loader，而不是 Holder，他职责仅仅是根据 提供的 Resource 类型，去做资源加载！
 * 这也是为什么，ResourceLoader 不叫 ResourceRegistry！
 */
public interface ResourceLoader {


    Resource getResource(String location);
}
