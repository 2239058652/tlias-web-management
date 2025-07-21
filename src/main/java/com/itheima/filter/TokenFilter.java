package com.itheima.filter;

import com.itheima.utils.CurrentHolder;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
//@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取请求路径
        String requestURI = request.getRequestURI();

        // 2. 判断是否登录 , 如果是登录请求, 直接放行
        if (requestURI.contains("/api/login") || requestURI.contains("/api/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 获取请求头中的令牌(token)
        String token = request.getHeader("token");

        // 4. 判断token是否存在,如果不存在, 说明没登陆 返回401
        if (token == null || token.isEmpty()) {
            log.info("没有登录 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        }

        // 5. 如果存在, 解析token, 判断token是否有效 , 如果无效, 返回401
        try {
            Claims claims = JwtUtils.parseToken(token);
            Integer empId = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setCurrentUserId(empId);
            log.info("当前登录用户id: {}", empId);
        } catch (Exception e) {
            log.info("token无效 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        }

        // 6. 如果有效, 放行
        log.info("token有效 放行");
        filterChain.doFilter(request, response);
        CurrentHolder.remove();
    }
}
