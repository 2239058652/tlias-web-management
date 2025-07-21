package com.itheima.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
//@Aspect
@Component
public class MyAspect1 {

    // 定义切入点表达式
    @Pointcut("execution(* com.itheima.service.impl.*.*(..))")
    public void pt() {}


    // 前置通知 在目标方法执行之前执行
    @Before("@annotation(com.itheima.anno.LogOperation)")
    public void before() {
        log.info("前置通知--------------");
    }

    // 环绕通知 在目标方法执行之前和之后执行，无论目标方法是否抛出异常都会执行
    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("环绕通知--------------before");
        Object result = pjp.proceed();
        log.info("环绕通知--------------after");
        return result;
    }

    // 后置通知 在目标方法执行之后执行
    @After("pt()")
    public void after() {
        log.info("后置通知--------------");
    }

    // 返回后置通知 在目标方法返回之后执行，如果目标方法抛出异常，则不执行
    @AfterReturning("execution(* com.itheima.service.impl.*.*(..))")
    public void afterReturning() {
        log.info("返回后置通知--------------");
    }

    // 异常通知 在目标方法抛出异常之后执行
    @AfterThrowing("execution(* com.itheima.service.impl.*.*(..))")
    public void afterThrowing() {
        log.info("异常通知--------------");
    }
}
