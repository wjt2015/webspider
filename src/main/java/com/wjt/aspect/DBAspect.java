package com.wjt.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class DBAspect {

    @Pointcut(value = "execution(* com.wjt.dao.*.*( .. ))")
    public void timeElapsedPointcut() {
    }


    //@Around(value = "execution(* com.wjt.dao.*.*( .. ))")
    @Around(value = "timeElapsedPointcut()")
    public Object timeElapsed(final ProceedingJoinPoint pjb) {
        final long start = System.currentTimeMillis();
        Object ret = null;
        final Object[] args = pjb.getArgs();
        try {
            ret = pjb.proceed(args);
        } catch (Throwable e) {
            log.info("pjb error!funcName={};args={};", pjb.getSignature(), Arrays.asList(args));
        }
        log.info("funcName={};args={};elapsed={}ms;ret={};", pjb.getSignature(), Arrays.asList(args), (System.currentTimeMillis() - start), ret);
        return ret;
    }
}
