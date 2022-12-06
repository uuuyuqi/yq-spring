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

    @Override
    public Resource getResource(String location) {
        Assert.notNull(location,"Location must not be null");

        // 在 spring 中，这个地方开了个口子，允许用户自定义 xx协议 的资源加载过程
        // 这个协议也是用户自定的

        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        else {
            try {
                // Try to parse the location as a URL...
                URL url = new URL(location);
                return new UrlResource(url);
            }
            catch (MalformedURLException ex) {
                // No URL -> resolve as resource path.
                throw new RuntimeException("resource [" + location + "] can not resolve");
            }
        }
    }




}
