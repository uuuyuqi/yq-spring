package com.yq.springframework.beans.factory.xml.core.io;

import cn.hutool.core.io.IoUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.yq.springframework.core.io.DefaultResourceLoader;
import com.yq.springframework.core.io.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 测试能不能把资源文件配置 生成 inputStream
 */
public class DefaultResourceLoaderTest {
    DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

    // 使用 resource 获取 url 资源
    @Test
    public void getResource_url() throws IOException {

        String url = "http://localhost/getXml";
        Resource resource = resourceLoader.getResource(url);

        String fromResource = IoUtil.read(resource.getInputStream(),"UTF-8");

        Assert.assertEquals(prepareContent(),fromResource);

    }

    // 使用 resource 获取 file:\\ 资源
    @Test
    public void getResource_file() throws IOException {

        String fileUrl = "file:\\D:\\study_proj\\1.java\\13.my-spring-framework\\yq-spring\\src\\test\\resources\\spring-context.xml";
        Resource resource = resourceLoader.getResource(fileUrl);

        String fromResource = IoUtil.read(resource.getInputStream(),"UTF-8");

        Assert.assertEquals(prepareContent(),fromResource);

    }

    // 使用 resource 获取文件路径资源
    @Test
    public void getResource_file2() throws IOException {

        String fileUrl = "src\\test\\resources\\spring-context.xml";
        Resource resource = resourceLoader.getResource(fileUrl);

        String fromResource = IoUtil.read(resource.getInputStream(),"UTF-8");

        Assert.assertEquals(prepareContent(),fromResource);

    }

    // 使用 resource 获取 classpath 资源
    @Test
    public void getResource_cp() throws IOException {

        String fileUrl = "classpath:spring-context.xml";
        Resource resource = resourceLoader.getResource(fileUrl);

        String fromResource = IoUtil.read(resource.getInputStream(),"UTF-8");

        Assert.assertEquals(prepareContent(),fromResource);

    }










    ////////////////////////////////////////
    // test tools

    // psvm --> generate http sever
    public static void main(String[] args) throws IOException {

        String respContent = prepareContent();

        // 创建 http 服务器, 绑定本地 80 端口
        // 第二个参数表示半连接对接
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(80), 0);
        httpServer.createContext("/getXml", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("req received: " + httpExchange.getRequestURI().getQuery());
                httpExchange.sendResponseHeaders(200, respContent.length());
                httpExchange.getResponseBody().write(respContent.getBytes());
            }
        });
        httpServer.start();
//            httpServer.stop(0);
    }


    public static String prepareContent(){
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n" +
                "                        http://www.springframework.org/schema/beans/spring-beans.xsd\">\n" +
                "    <bean id=\"tb\" class=\"com.yq.springframework.test.Sample.beans.TestBean\" >\n" +
                "        <property name=\"id\" value=\"1000\" />\n" +
                "        <property name=\"name\" value=\"ZDK\" />\n" +
                "    </bean>\n" +
                "    <bean id=\"tbp\" class=\"com.yq.springframework.test.Sample.beans.TestBeanPlus\">\n" +
                "        <property name=\"testBean\" ref=\"tb\" />\n" +
                "    </bean>\n" +
                "</beans>";


        return content;
    }
}