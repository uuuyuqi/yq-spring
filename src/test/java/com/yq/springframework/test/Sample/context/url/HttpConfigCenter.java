package com.yq.springframework.test.Sample.context.url;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpConfigCenter {
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
