package com.itheima.aop;

import com.itheima.mapper.OperateLogMapper;
import com.itheima.pojo.OperateLog;
import com.itheima.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class OperateLogAspect {
    @Pointcut("@annotation(com.itheima.anno.ALog)")
    public void logAnnotation() {}

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Before("execution(* com.itheima.controller.*.*(..))")
    public void beforeAllControllerMethods() {
        log.info("Before controller method+++++++++++++++++++++++++++++++");
    }

    @Around("logAnnotation()")
    public Object logOperation(ProceedingJoinPoint pjp) throws Throwable {
        log.info("环绕通知****************--------------before");

        long startTime = System.currentTimeMillis();

        //执行目标方法
        Object result = pjp.proceed();

        // 计算执行时间
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;


        //构建日志实体
        OperateLog logs = new OperateLog();
        logs.setOperateEmpId(getCurrentUserId());
        logs.setOperateTime(LocalDateTime.now());
        logs.setClassName(pjp.getTarget().getClass().getName());
        logs.setMethodName(pjp.getSignature().getName());
        logs.setMethodParams(Arrays.toString(pjp.getArgs()));
        logs.setReturnValue(result != null ? result.toString() : "void");
        logs.setCostTime(costTime);

        //保存日志
        log.info("日志信息******************************：{}", logs);
        operateLogMapper.insert(logs);
        return result;
    }

    private Integer getCurrentUserId() {
        // TODO: 获取当前登录用户ID
        return CurrentHolder.getCurrentUserId();
    }
}
