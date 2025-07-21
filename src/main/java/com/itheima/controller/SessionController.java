package com.itheima.controller;

import com.itheima.pojo.Result;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController

public class SessionController {

    // 设置cookie
    @GetMapping("/api/c1")
    public Result cookie1(HttpServletResponse response) {
        response.addCookie(new Cookie("login_username", "itheima"));
        return Result.success();
    }

    // 获取cookie
    @GetMapping("/api/c2")
    public Result cookie2(HttpServletRequest resquest) {
        Cookie[] cookies = resquest.getCookies();
        for (Cookie cook : cookies) {
            if(cook.getName().equals("login_username")) {
                System.out.println("login_username: " + cook.getValue());
            }
        }
        return Result.success();
    }

    @GetMapping("/api/s1")
    public Result session1(HttpSession session) {
        log.info("HttpSession-s1: {}", session.hashCode());

        session.setAttribute("loginUser", "dcinlgx");  // 设置session
        return Result.success();
    }

    @GetMapping("/api/s2")
    public Result session2(HttpSession session) {
        log.info("HttpSession-s2: {}", session.hashCode());

        Object loginUser = session.getAttribute("loginUser");  // 获取session
        log.info("session-loginUser: {}", loginUser);
        return Result.success(loginUser);
    }

}
