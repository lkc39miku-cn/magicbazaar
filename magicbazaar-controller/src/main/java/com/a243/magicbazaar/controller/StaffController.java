package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.service.StaffService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.StaffParam;
import com.a243.magicbazaar.view.vo.StaffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "admin/staff")
public class StaffController {
    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping(value = "list")
    public Result<List<StaffVo>> list(StaffParam staffParam) {
        return staffService.list(staffParam);
    }

    @PostMapping(value = "phonecode")
    public void phoneCode(HttpServletResponse response, String phone) {
        staffService.phoneCode(response, phone);
    }

    @PostMapping(value = "emailcode")
    public void emailCode(HttpServletResponse response, String email) {
        staffService.emailCode(response, email);
    }

    @PostMapping(value = "add")
    public <T> Result<T> add(HttpServletRequest request, HttpServletResponse response, StaffParam staffParam) {
        return staffService.add(request, response, staffParam);
    }

    @PostMapping(value = "update")
    public <T> Result<T> update(HttpServletRequest request, HttpServletResponse response, StaffParam staffParam) {
        return staffService.update(request, response, staffParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(StaffParam staffParam) {
        return staffService.on(staffParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(StaffParam staffParam) {
        return staffService.off(staffParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(StaffParam staffParam) {
        return staffService.delete(staffParam);
    }

    @PostMapping(value = "role")
    public <T> Result<T> role(StaffParam staffParam) {
        return staffService.role(staffParam);
    }

    @PostMapping(value = "info")
    public Result<StaffVo> info() {
        return staffService.info();
    }

    @PostMapping(value = "checkname")
    public <T> Result<T> checkName(StaffParam staffParam) {
        return staffService.checkName(staffParam);
    }

    @PostMapping(value = "emailcheck")
    public <T> Result<T> emailCheck(StaffParam staffParam) {
        return staffService.emailCheck(staffParam);
    }

    @PostMapping(value = "checkpassword")
    public <T> Result<T> checkPassword(StaffParam staffParam) {
        return staffService.checkPassword(staffParam);
    }

    @PostMapping(value = "changepassowrd")
    public <T> Result<T> changePassword(StaffParam staffParam) {
        return staffService.changePassword(staffParam);
    }
}
