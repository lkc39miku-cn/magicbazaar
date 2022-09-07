package com.a243.magicbazaar.config.log;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 日志
 * 统一日志处理切面
 */
@Aspect
@Component
@Order(1)
public class WebLogAspect {
    /**
     * 日志记录工厂
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);

    /**
     * 切入点
     */
    @Pointcut(value = "execution(public * com.a243.magicbazaar.controller..*.*(..))")
    public void webLog(){}

    /**
     * 方法调用之前执行
     * @param joinPoint joinPoint
     * @throws Throwable 异常
     */
    @Before(value = "webLog()")
    public void before(JoinPoint joinPoint) throws Throwable {

    }

    /**
     * 方法返回结果后执行
     * @param result 返回结果
     */
    @AfterReturning(value = "webLog()", returning = "result")
    public void afterReturning(Object result) {
    }

    /**
     * 围绕整个方法执行
     * @param proceedingJoinPoint proceedingJoinPoint
     * @return 返回结果
     * @throws Throwable 异常
     */
    @Around(value = "webLog()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTIme = System.currentTimeMillis();

        // 获取当前请求对象
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();

        // 记录请求信息
        com.a243.magicbazaar.config.log.WebLog webLog = new com.a243.magicbazaar.config.log.WebLog();
        Object result = proceedingJoinPoint.proceed();
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        long endTime = System.currentTimeMillis();

        String urlStr = httpServletRequest.getRequestURL().toString();

        webLog.setBasePath(StrUtil.removePrefix(urlStr, URLUtil.url(urlStr).getPath()));
        webLog.setIp(httpServletRequest.getRemoteUser());
        webLog.setMethod(httpServletRequest.getMethod());
        webLog.setParameter(getParameter(method, proceedingJoinPoint.getArgs()));
        webLog.setResult(result);
        webLog.setSpendTime((int)(endTime - startTIme));
        webLog.setStartTime(startTIme);
        webLog.setUri(httpServletRequest.getRequestURI());
        webLog.setUrl(httpServletRequest.getRequestURL().toString());

        LOGGER.info("{}", JSONUtil.parse(webLog));
        return result;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     * @param method 方法
     * @param args 传入参数
     * @return 请求参数
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i<parameters.length; i++) {
            // 将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);

            if (requestBody != null) {
                argList.add(args[i]);
            }

            // 将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);

            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();

                if (!StrUtil.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }

                map.put(key, args[i]);
                argList.add(map);
            }
        }

        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }

}
