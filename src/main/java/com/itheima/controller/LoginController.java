package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.LoginInfo;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private EmpService  empService;

    /**
     * 登录
     * */
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
      log.info("登录: {}", emp);
        LoginInfo info = empService.login(emp);
        if(info != null) {
            return Result.success(info);
        }
        return Result.error("登录失败,请检查用户名和密码");
    }
}
