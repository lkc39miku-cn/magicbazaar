package com.a243.magicbazaar.config.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
@Order(2)
public class LogAspect {

    @Pointcut("execution(public * com.a243.magicbazaar.controller..*.*(..))")
    public void logPointCut(){
        // empty
    }

    @Around(value = "logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long time = System.currentTimeMillis() - begin;

        System.out.println();
        log.info("===============log start===============");
        log.info("verify:{}", "success");

        log.info("time:{}", LocalDateTime.now());

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        log.info("method:{}", className + "." + methodName + "()");

        Object[] args = joinPoint.getArgs();
        Object[] arguments  = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        String paramter;
        try {
            paramter = JSONObject.toJSONString(arguments);
        } catch (Exception e) {
            paramter = Arrays.toString(arguments);
        }

        log.info("param:{}", paramter);
        log.info("result:{}", result);

        log.info("execution time:{}ms", time);
        log.info("===============log end===============");

        return result;
    }

    @AfterThrowing(value = "logPointCut()", throwing = "error")
    public void afterThrowing(JoinPoint joinPoint, Exception error) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        System.out.println();
        log.error("===============log start===============");
        log.error("verify:{}", "error");

        log.error("time:{}", LocalDateTime.now());

        log.error("error:{}", error.getMessage());
        log.error("error info:{}", error.getClass());

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        log.error("method:{}", className + "." + methodName + "()");

        Object[] args = joinPoint.getArgs();
        Object[] arguments  = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        String paramter;
        try {
            paramter = JSONObject.toJSONString(arguments);
        } catch (Exception e) {
            paramter = Arrays.toString(arguments);
        }

        log.error("params:{}", paramter);
        log.error("===============log end===============");
    }
}
