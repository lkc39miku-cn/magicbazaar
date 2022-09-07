package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.MenuPermissionService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.MenuParam;
import com.a243.magicbazaar.view.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/menu/permission")
public class MenuPermissionController {
    private MenuPermissionService menuPermissionService;

    @Autowired
    public MenuPermissionController(MenuPermissionService menuPermissionService) {
        this.menuPermissionService = menuPermissionService;
    }

    @PostMapping(value = "parent")
    public Result<List<MenuVo>> parent() {
        return menuPermissionService.parent();
    }

    @PostMapping(value = "order")
    public Result<List<MenuVo>> order(MenuParam menuParam) {
        return menuPermissionService.order(menuParam);
    }

    @PostMapping(value = "child")
    public Result<List<MenuVo>> child(MenuParam menuParam) {
        return menuPermissionService.child(menuParam);
    }
}
