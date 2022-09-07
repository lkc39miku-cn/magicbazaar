package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.dto.MenuPermission;
import com.a243.magicbazaar.service.RolePermissionService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.RolePermissionParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/role/permission")
public class RolePermissionController {
    private final RolePermissionService rolePermissionService;

    @Autowired
    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @RequestMapping(value = "list")
    public Result<List<MenuPermission>> list(RolePermissionParam rolePermissionParam) {
        return rolePermissionService.list(rolePermissionParam);
    }
}
