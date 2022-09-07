package com.a243.magicbazaar.controller.reception;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.dao.entity.User;
import com.a243.magicbazaar.service.reception.ReUserService;
import com.a243.magicbazaar.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "reception")
public class ReTokenController {

    private final ReUserService reUserService;

    @Autowired
    public ReTokenController(ReUserService reUserService) {
        this.reUserService = reUserService;
    }

    @RequestMapping(value = "login")
    public String login(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
            if (!StrUtil.isBlank(token)) {
                User user = reUserService.checkUser(token);
                if (user != null) {
                    return "redirect:/reception/url/index";
                }
            }
        }
        return "reception/login";
    }

    @ResponseBody
    @PostMapping(value = "token/login")
    public <T> Result<T> tokenLogin(User user, String autoLogin, HttpServletRequest request, HttpServletResponse response) {
        return reUserService.findUserByNameAndPassword(user, autoLogin, request, response);
    }

    @ResponseBody
    @PostMapping(value = "token/logout")
    public <T> Result<T> logout(HttpServletRequest request) {
        return reUserService.logout(request);
    }
}
