package com.yq.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * spring 资源顶级接口
 * 按照 spring 的玩法，BF 在真正运转之前，需要将 BeanDefinition 扫描出来，并放入 BD holder (即 BeanDefinitionRegistry) 中
 * 那么问题来了，这些 BD 从哪儿获取？ 肯定是来自配置文件、或者也可以来自于配置中心下发？ 无论如何这些 BD 从哪儿来，最终一定是一些
 * 配置信息整理出来，那显然是以 流 的形式读取。
 *
 * 所以个人觉得，这个所谓的 Resource，其实就是 —— BeanDefinitionResource !
 *
 * 显而易见，spring 必须拥有一套资源加载机制 ！
 */
public interface Resource {
    /**
     * 直接获取流
     * 在 spring 的源码设计中，流的形式获取是一个更顶级的接口 InputStreamSource，表示拥有获取输入流行为
     * @return is
     */
    InputStream getInputStream() throws IOException;

    /**
     * nio channel 的形式读取
     * @return nio channel
     * @throws IOException IOE
     */
    default ReadableByteChannel readableChannel() throws IOException {
        return Channels.newChannel(getInputStream());
    }

    // 当然，在 spring 源码中，还有别的方法，比如 contentLength、 getFilename、 getURI、 isOpen等
}
