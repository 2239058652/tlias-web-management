package com.itheima.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration  // 声明这是一个配置类
public class RestTemplateConfig {

    @Bean  // 向 Spring 容器中注入 RestTemplate Bean
    public RestTemplate restTemplate() {
        // 创建工厂类，用于配置连接和超时
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        // 设置超时时间（单位：毫秒）
        factory.setConnectTimeout(10000); // 连接超时：10秒
        factory.setReadTimeout(60000);    // 读取超时：60秒

        // 如果你使用 HTTP 代理（例如 Clash、V2Ray、Surge、Shadowrocket）
        // 修改成你本地代理监听的地址和端口（如127.0.0.1:7890）
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7897));
        factory.setProxy(proxy); // 配置代理

        // 返回带有代理和超时设置的 RestTemplate
        return new RestTemplate(factory);
    }
}
