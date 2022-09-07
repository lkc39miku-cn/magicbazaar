package com.a243.magicbazaar.controller;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.dto.ChoosePermission;
import com.a243.magicbazaar.dao.entity.Role;
import com.a243.magicbazaar.service.RoleService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.RoleParam;
import com.a243.magicbazaar.view.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/role")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping(value = "list")
    public Result<List<RoleVo>> list(RoleParam roleParam) {
        return roleService.list(roleParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(RoleParam roleParam) {
        return roleService.on(roleParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(RoleParam roleParam) {
        return roleService.off(roleParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(RoleParam roleParam) {
        return roleService.delete(roleParam);
    }

    private Long roleValue;

    @PostMapping(value = "add")
    public <T> Result<T> add(Role role) {
        return roleService.add(role);
    }

    @PostMapping(value = "update")
    public <T> Result<T> update(Role role) {
        return roleService.update(role);
    }

    @PostMapping(value = "getpermission")
    public Result<List<ChoosePermission>> getPermission(RoleParam roleParam) {
        return roleService.getPermission(roleParam);
    }

    @PostMapping(value = "rolevalue")
    public <T> Result<T> roleValue(RoleParam roleParam) {
        if (!StrUtil.isBlank(roleParam.getId())) {
            roleValue = Long.parseLong(roleParam.getId());
            return Result.ok();
        } else {
            return Result.fail("系统错误");
        }
    }

    @PostMapping(value = "updaterole")
    public <T> Result<T> updateRole(@RequestBody List<ChoosePermission> choosePermissionList) {
        return roleService.updateRole(roleValue, choosePermissionList);
    }

}
