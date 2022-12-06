package com.yq.springframework.core.io;

import cn.hutool.core.lang.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.channels.ReadableByteChannel;

/**
 * URL 资源
 */
public class UrlResource implements Resource{

    // 这里和 spring 源码设计出入较大
    // 源码中此处为 private，继承结构更为复杂
    private final URI uri;

    private final URL url;


    public UrlResource(URI uri) throws MalformedURLException {
        Assert.notNull(uri, "URI must not be null");
        this.uri = uri;
        this.url = uri.toURL();
    }

    public UrlResource(URL url) {
        Assert.notNull(url, "URL must not be null");
        this.uri = null;
        this.url = url;
    }

    public UrlResource(String path) throws MalformedURLException {
        Assert.notNull(path, "Path must not be null");
        this.uri = null;
        this.url = new URL(path);
    }

    /**
     * 从 URL 中获取 is
     * @return is
     * @throws IOException IOE
     */
    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection con = this.url.openConnection();
        try {
            return con.getInputStream();
        }
        // spring 源码中，此处对 http url 进行单独的关闭处理
        catch (IOException ex) {
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).disconnect();
            }
            throw ex;
        }
    }
}
