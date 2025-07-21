package com.itheima.config;

import com.itheima.interceptor.DemoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private DemoInterceptor demoInterceptor;

    /**
     * 配置跨域请求
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*").allowCredentials(false);
    }

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(demoInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**", "/index.html");
    }

    /**
     * 配置静态资源处理器（优先级最高）
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").setCachePeriod(0);
    }

    /**
     * 配置路由转发：仅对真正的前端路由进行转发
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 只转发根路径和没有扩展名的路径（前端路由通常没有扩展名）
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/{path:[^\\.]+}").setViewName("forward:/index.html");
        // 根路径转发到 index.html
        registry.addViewController("/").setViewName("forward:/index.html");
        // 所有不含 "." 的路径（前端路由）转发到 index.html
        registry.addViewController("/{path:[^.]*}").setViewName("forward:/index.html");
    }
}