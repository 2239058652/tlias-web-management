package com.itheima.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

//@WebFilter(urlPatterns = "/*")  //  设置过滤器拦截的url  *：拦截所有请求
@Slf4j
public class DemoFilter implements Filter {

    // 过滤器的初始化方法 ：在服务器启动时执行一次
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init过滤器初始化.........");
    }

    // 过滤器的执行方法：每次请求被拦截的url时执行 会执行多次
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("doFilter过滤器执行.........,请求被拦截");
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    // 销毁方法：在服务器关闭时执行一次
    @Override
    public void destroy() {
        log.info("destroy过滤器销毁.........");
    }
}
