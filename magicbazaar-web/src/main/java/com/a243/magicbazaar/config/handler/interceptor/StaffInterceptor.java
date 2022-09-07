package com.a243.magicbazaar.config.handler.interceptor;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.service.StaffService;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.util.token.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class StaffInterceptor implements HandlerInterceptor {

    private final StaffService staffService;

    @Autowired
    public StaffInterceptor(StaffService staffService) {
        this.staffService = staffService;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = null;

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            response.sendRedirect("/admin/login");
            return false;
        }

        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        if (StrUtil.isBlank(token)) {
            response.sendRedirect("/admin/login");
            return false;
        }

        Staff staff = staffService.checkStaff(token);
        if (staff == null) {
            response.sendRedirect("/admin/login");
            return false;
        }

        System.out.println();
        log.info("===============request start===============");

        log.info("request ip:{}", IpUtil.getIpAddr(request));
        log.info("request uri:{}", request.getRequestURI());
        log.info("request method:{}", request.getMethod());
        log.info("token:{}", token);

        log.info("===============request end===============");
        System.out.println();

        StaffThreadLocal.set(staff);
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        StaffThreadLocal.remove();
    }
}
