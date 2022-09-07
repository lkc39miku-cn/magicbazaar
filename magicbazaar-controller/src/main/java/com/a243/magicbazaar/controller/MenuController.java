package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.Menu;
import com.a243.magicbazaar.service.MenuService;
import com.a243.magicbazaar.util.TreeUtil;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.MenuParam;
import com.a243.magicbazaar.view.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "admin/menu")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping(value = "loadIndexMenuLeft")
    public Map<String, Object> loadIndexMenuLeft(MenuVo menuVo) {

        // 创建 map 集合 保存 menuInfo 信息
        Map<String, Object> map = new LinkedHashMap<>();
        // 创建 map 集合 保存 homeInfo 信息
        Map<String, Object> homeInfo = new LinkedHashMap<>();
        // 创建 map 集合 保存 logoInfo 信息
        Map<String, Object> logoInfo = new LinkedHashMap<>();

        // 调用查询菜单的方法
        List<Menu> menuList = menuService.findMenuList(menuVo);
        // 创建集合 保存菜单关系
        List<MenuVo> menuNodeList = new ArrayList<>();

        // 循环遍历
        for (Menu menu : menuList) {
            MenuVo menuNode = new MenuVo()
                    .setId(menu.getId())
                    .setPid(menu.getPid())
                    .setTitle(menu.getTitle())
                    .setHref(menu.getHref())
                    .setIcon(menu.getIcon())
                    .setTarget(menu.getTarget());
            menuNodeList.add(menuNode);
        }

        // 保存 homeInfo 信息
        homeInfo.put("title", "主页");
        homeInfo.put("href", "/url/index");

        // 保存 logoInfo 信息
        logoInfo.put("title", "电商管理系统");
        logoInfo.put("image", "/images/logo.png");
        logoInfo.put("href", "/index");

        // 保存 menuInfo 信息
        map.put("menuInfo", TreeUtil.toTree(menuNodeList, 0L));
        map.put("homeInfo", homeInfo);
        map.put("logoInfo", logoInfo);

        return map;
    }

    @PostMapping(value = "add")
    public <T> Result<T> add(MenuParam menuParam) {
        return menuService.add(menuParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(MenuParam menuParam) {
        return menuService.delete(menuParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(MenuParam menuParam) {
        return menuService.on(menuParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(MenuParam menuParam) {
        return menuService.off(menuParam);
    }

    @PostMapping(value = "update")
    public <T> Result<T> update(MenuParam menuParam) {
        return menuService.update(menuParam);
    }

    @PostMapping(value = "parentmenuid")
    public Result<String> getParentMenuId(Long id) {
        return menuService.getParentMenuId(id);
    }

    @PostMapping(value = "interfacemenuid")
    public Result<String> getIInterFaceMenuId(Long id) {
        return menuService.getIInterFaceMenuId(id);
    }
}
