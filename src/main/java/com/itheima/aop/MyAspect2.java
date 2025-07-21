package com.itheima.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
//@Aspect
@Component
public class MyAspect2 {

    @Before("execution(* com.itheima.service.*.*(..))")
    public void before(JoinPoint joinPoint) {
        log.info("MyAspect2 before......");
        // 获取目标对象
        Object target = joinPoint.getTarget();
        log.info("target:{}", target);

        // 获取目标类
        String className = joinPoint.getTarget().getClass().getName();
        log.info("className:{}", className);

        // 获取方法
        String methodName = joinPoint.getSignature().getName();
        log.info("methodName:{}", methodName);

        // 获取参数
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info("arg:{}", arg);
        }

    }

    @Around("execution(* com.itheima.service.*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("MyAspect2 around......");

        Object result = pjp.proceed();
        log.info("MyAspect2 around after......");
        return result;
    }
}
