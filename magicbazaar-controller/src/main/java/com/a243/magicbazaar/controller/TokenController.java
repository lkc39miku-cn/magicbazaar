package com.a243.magicbazaar.controller;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.service.StaffService;
import com.a243.magicbazaar.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "admin")
public class TokenController {

    private StaffService staffService;

    @Autowired
    public TokenController(StaffService staffService) {
        this.staffService = staffService;
    }

    @RequestMapping(value = "index")
    public String index() {
        return "backstage/admin/index";
    }

    @RequestMapping(value = "login")
    public String login(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
            if (!StrUtil.isBlank(token)) {
                Staff staff = staffService.checkStaff(token);
                if (staff != null) {
                    return "redirect:/admin/index";
                }
            }
        }
        return "backstage/login";
    }

    @ResponseBody
    @PostMapping(value = "token/login")
    public <T> Result<T> tokenLogin(Staff staff, String autoLogin, HttpServletRequest request, HttpServletResponse response) {
        return staffService.findStaffByNameAndPassword(staff, autoLogin, request, response);
    }

    @ResponseBody
    @PostMapping(value = "token/logout")
    public <T> Result<T> logout(HttpServletRequest request) {
        return staffService.logout(request);
    }
}
