package com.itheima.interceptor;

import com.itheima.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径
        String requestURI = request.getRequestURI();

        // 判断请求路径是否包含登录
        if(requestURI.contains("/login")){
            log.info("登录接口，放行");
            return true;
        }

        //   获取请求头中的token
        String token = request.getHeader("token");
        // 判断token是否为空 或者为null
        if(token == null || token.isEmpty()){
            log.info("token为空，拦截 401");
            response.setStatus(401);
            return false;
        }

        //ruguo token不为空,校验token是否合法
        try{
            JwtUtils.parseToken(token);
        }catch (Exception e){
            log.info("token不合法，拦截 401");
            response.setStatus(401);
            return false;
        }

        // token合法，放行
        log.info("token合法，放行");
        return true;
    }
}
