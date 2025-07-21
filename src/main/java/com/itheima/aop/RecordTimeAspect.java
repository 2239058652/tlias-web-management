package com.itheima.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
//@Aspect // 声明切面 标识当前是一个AOP类
//@Component // 交给Spring容器管理
public class RecordTimeAspect {

    @Around("execution(* com.itheima.service.impl.*.*(..))")
    public Object recordTime(ProceedingJoinPoint pjp) throws Throwable{
        // 记录方法运行的开始时间
        long begin = System.currentTimeMillis();

        // 执行目标方法
        Object result = pjp.proceed();

        // 记录方法运行的结束时间
        long end = System.currentTimeMillis();
        log.info("方法{}执行耗时：{}ms 运行时间：{}ms", pjp.getSignature(), end - begin);
        return result; // 返回目标方法的执行结果
    }
}
