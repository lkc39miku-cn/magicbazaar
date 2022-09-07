package com.a243.magicbazaar.service.impl;

import com.a243.magicbazaar.dao.dto.MenuPermission;
import com.a243.magicbazaar.dao.entity.Menu;
import com.a243.magicbazaar.dao.entity.Permission;
import com.a243.magicbazaar.dao.entity.PermissionMenu;
import com.a243.magicbazaar.dao.mapper.MenuMapper;
import com.a243.magicbazaar.dao.mapper.PermissionMapper;
import com.a243.magicbazaar.dao.mapper.PermissionMenuMapper;
import com.a243.magicbazaar.dao.mapper.RolePermissionMapper;
import com.a243.magicbazaar.service.RolePermissionService;
import com.a243.magicbazaar.util.convert.impl.PermissionConvert;
import com.a243.magicbazaar.util.convert.impl.RolePermissionConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.RolePermissionParam;
import com.a243.magicbazaar.view.vo.PermissionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {
    private final RolePermissionMapper rolePermissionMapper;
    private final RolePermissionConvert rolePermissionConvert;
    private MenuMapper menuMapper;
    private PermissionMapper permissionMapper;
    private PermissionMenuMapper permissionMenuMapper;
    private PermissionConvert permissionConvert;

    @Autowired
    public RolePermissionServiceImpl(RolePermissionMapper rolePermissionMapper, RolePermissionConvert rolePermissionConvert) {
        this.rolePermissionMapper = rolePermissionMapper;
        this.rolePermissionConvert = rolePermissionConvert;
    }

    @Autowired
    public void setMenuMapper(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Autowired
    public void setPermissionMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Autowired
    public void setPermissionMenuMapper(PermissionMenuMapper permissionMenuMapper) {
        this.permissionMenuMapper = permissionMenuMapper;
    }

    @Autowired
    public void setPermissionConvert(PermissionConvert permissionConvert) {
        this.permissionConvert = permissionConvert;
    }

    @Override
    public Result<List<MenuPermission>> list(RolePermissionParam rolePermissionParam) {
        if (rolePermissionParam.getTree()) {
            List<MenuPermission> menuPermissionList = new ArrayList<>();

            // 主菜单
            List<Menu> menus = menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id", 0));
            // 加入主菜单
            for (Menu menu : menus) {
                menuPermissionList.add(new MenuPermission().setId(menu.getId()).setName(menu.getTitle()).setIsPermission(0).setDeleteStatus(menu.getDeleteStatus()).setPublishStatus(menu.getStatus()).setParentId(menu.getPid()).setCreateTime(menu.getCreateTime()).setUpdateTime(menu.getUpdateTime()));
            }

            // 菜单信息添加
            List<MenuPermission> menuChildList = new ArrayList<>();
            for (MenuPermission menuChilds : menuPermissionList) {
                List<Menu> menuList = menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id", menuChilds.getId()));
                for (Menu menuChild : menuList) {
                    menuChildList.add(new MenuPermission().setId(menuChild.getId()).setName(menuChild.getTitle()).setIsPermission(1).setDeleteStatus(menuChild.getDeleteStatus()).setPublishStatus(menuChild.getStatus()).setParentId(menuChild.getPid()).setCreateTime(menuChild.getCreateTime()).setUpdateTime(menuChild.getUpdateTime()));
                }
            }

            // 子菜单信息添加
            List<MenuPermission> menuChildrenList = new ArrayList<>();
            for (MenuPermission menuPermission : menuChildList) {
                // 子菜单信息
                List<Menu> selectList = menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id", menuPermission.getId()));
                for (Menu menuChild : selectList) {
                    menuChildrenList.add(new MenuPermission().setId(menuChild.getId()).setName(menuChild.getTitle()).setIsPermission(2).setDeleteStatus(menuChild.getDeleteStatus()).setPublishStatus(menuChild.getStatus()).setParentId(menuChild.getPid()).setMenuValue(menuChild.getHref()).setCreateTime(menuChild.getCreateTime()).setUpdateTime(menuChild.getUpdateTime()));
                }
            }

            // 接口信息添加
            List<MenuPermission> menuPermissionChildList = new ArrayList<>();
            for (MenuPermission menuChildren : menuChildrenList) {
                List<Long> permissionIdList = permissionMenuMapper.selectList(new QueryWrapper<PermissionMenu>().eq("menu_id", menuChildren.getId())).stream().map(PermissionMenu::getPermissionId).collect(Collectors.toList());
                if (permissionIdList.size() != 0) {
                    List<Permission> permissionList = permissionMapper.selectList(new QueryWrapper<Permission>().in("id", permissionIdList));

                    // 转vo获取menuId
                    List<PermissionVo> permissionVoList = permissionConvert.convert(permissionList);

                    // 添加接口
                    for (PermissionVo permissionVo : permissionVoList) {
                        menuPermissionChildList.add(new MenuPermission().setFalseId(permissionVo.getId()).setName(permissionVo.getName()).setPermissionValue(permissionVo.getPath()).setDeleteStatus(permissionVo.getDeleteStatus()).setIsPermission(3).setParentId(permissionVo.getMenuId()).setCreateTime(permissionVo.getCreateTime()).setUpdateTime(permissionVo.getUpdateTime()));
                    }
                }
            }

            menuPermissionList.addAll(menuChildList);
            menuPermissionList.addAll(menuChildrenList);
            menuPermissionList.addAll(menuPermissionChildList);
            return Result.ok(menuPermissionList.size(), menuPermissionList);
        } else {
            return Result.fail("系统错误");
        }
    }
}
