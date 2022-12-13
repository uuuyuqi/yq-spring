package com.yq.springframework.core.io;

import cn.hutool.core.lang.Assert;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * ResourceLoader 的默认实现，具有 load Resource 的行为
 *
 */
public class DefaultResourceLoader implements ResourceLoader {

    private final String CLASSPATH_URL_PREFIX = "classpath:";
    private final String FILE_PATH_PREFIX = "file:";

    @Override
    public Resource getResource(String location) {
        Assert.notNull(location,"Location must not be null");

        // 在 spring 中，这个地方开了个口子，允许用户自定义 xx协议 的资源加载过程
        // 这个协议的处理也是用户自定的

        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        // 在 spring 源码中，file 文件系统是直接通过:  URL + 类型判断为 file 的方式，直接放到 else 中处理的
        // 这里为了简化，直接加了一层 file 的判断
        else if (location.startsWith(FILE_PATH_PREFIX)){
            return new FileSystemResource(location.substring(FILE_PATH_PREFIX.length()));
        }
        else {
            try {
                // Try to parse the location as a URL...
                URL url = new URL(location);
                return new UrlResource(url);
            }
            catch (MalformedURLException ex) {
                // 按照文件系统的方式加载
                return new FileSystemResource(location);
            }
        }
    }




}
