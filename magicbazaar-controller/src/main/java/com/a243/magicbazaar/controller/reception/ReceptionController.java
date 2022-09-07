package com.a243.magicbazaar.controller.reception;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.User;
import com.a243.magicbazaar.service.reception.ReUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "reception/url")
public class ReceptionController {
    private final ReUserService reUserService;

    @Autowired
    public ReceptionController(ReUserService reUserService) {
        this.reUserService = reUserService;
    }

    @RequestMapping(value = "index")
    public String index() {
        return "reception/index";
    }

    @RequestMapping(value = "register")
    public String register(HttpServletRequest request) {
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
        return "reception/register";
    }

    @RequestMapping(value = "category")
    public String category() {
        return "reception/category";
    }

    @RequestMapping(value = "cart")
    public String cart() {
        return "reception/cart";
    }

    @RequestMapping(value = "blog")
    public String blog() {
        return "reception/blog";
    }

    @RequestMapping(value = "singleblog")
    public String singleBlog() {
        return "reception/singleblog";
    }

    @RequestMapping(value = "stars")
    public String collet() {
        return "reception/stars";
    }

    @RequestMapping(value = "online")
    public String online() {
        return "reception/message";
    }

}
