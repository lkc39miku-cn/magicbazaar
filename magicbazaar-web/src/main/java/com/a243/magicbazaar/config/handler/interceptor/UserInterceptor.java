package com.a243.magicbazaar.config.handler.interceptor;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.dao.entity.User;
import com.a243.magicbazaar.dao.mapper.UserMapper;
import com.a243.magicbazaar.service.reception.ReUserService;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.util.thread.UserThreadLocal;
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
public class UserInterceptor implements HandlerInterceptor {
    private ReUserService reUserService;
    private UserMapper userMapper;

    @Autowired
    public void setReUserService(ReUserService reUserService) {
        this.reUserService = reUserService;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = null;

        Cookie[] cookies = request.getCookies();

        boolean check = true;

        if (cookies == null) {
//            response.sendRedirect("/reception/login");
//            return false;
            check = false;
        }

        if (check) {
            for (Cookie cookie : cookies) {
                if ("userToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (StrUtil.isBlank(token)) {
//            response.sendRedirect("/reception/login");
//            return false;
            check = false;
        }

        if (check) {
            User user = reUserService.checkUser(token);
            if (user == null) {
//                response.sendRedirect("/reception/login");
//                return false;
                check = false;
            }

            if (check) {
                UserThreadLocal.set(userMapper.selectById(user.getId()));
                return true;
            }
        }
        if (request.getRequestURI().equals("/reception/commodity/addcart") ||
                request.getRequestURI().equals("/reception/commodity/cartnum") ||
                request.getRequestURI().equals("/reception/commodity/loadcart") ||
                request.getRequestURI().equals("/reception/commodity/deletecart") ||
                request.getRequestURI().equals("/reception/user/checklogin")) {
            return true;
        } else {
            response.sendRedirect("/reception/login");
            return false;
        }
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
