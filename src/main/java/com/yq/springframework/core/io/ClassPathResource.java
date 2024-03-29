package com.yq.springframework.core.io;

import cn.hutool.core.lang.Assert;
import com.yq.springframework.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

// 读取 classpath 下的文件，一般 spring 项目打出来的 fat jar，配置文件大都在 classpath 下面
// 真正 spring 在查找配置文件时，默认会查以下几个位置：
//  - file:./config/
//  - file:./config/*/
//  - file:./
//  - classpath:/config/
//  - classpath:/
//
// 源码可以参考：
//  - ConfigFileApplicationListener#getSearchLocations
//  - ConfigFileApplicationListener#getSearchNames
//
// 通过查询源码，可以发现：配置文件的目录，和文件名都是可以配置的

/**
 * 显然 ClassPathResource 代表了 classpath 目录下的配置文件资源
 *
 * 在 spring 源码中，这个地方还支持使用 class 来加载资源，和 classloader 相比,
 * class.getResource 默认加载的是当前 class 路径(具体到当前包)下的 resource
 */
public class ClassPathResource implements Resource{


    private final String path;

    private final ClassLoader classLoader;


    public ClassPathResource(String path) {
        this(path,null);
    }


    /**
     *
     * @param path classpath 中的相对或绝对路径
     * @param classLoader 用来载资源的类加载器
     */
    public ClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path,"Path must not be null");
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }



    /**
     * 以流的形式获取类路径下的资源
     * 取 is 的优先级是：
     * class > classloader > 默认 AppCL 做资源加载(AppCl=null -> bootStrap)
     * @return is
     */
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is;
        if (this.classLoader != null) {
            is = this.classLoader.getResourceAsStream(path);
        }
        else {
            is = ClassLoader.getSystemResourceAsStream(path);
        }

        if (is == null)
            throw new FileNotFoundException("[" +  this.path + "] cannot be opened because it does not exist");

        return is;
    }

}
