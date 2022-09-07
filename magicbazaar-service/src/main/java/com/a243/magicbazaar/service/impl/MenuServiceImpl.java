package com.a243.magicbazaar.service.impl;

import com.a243.magicbazaar.dao.entity.*;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.service.MenuService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.Page;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.view.param.MenuParam;
import com.a243.magicbazaar.view.vo.MenuVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    private final MenuMapper menuMapper;
    private final PermissionMapper permissionMapper;
    private PermissionMenuMapper permissionMenuMapper;
    private StaffRoleMapper staffRoleMapper;
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    public MenuServiceImpl(MenuMapper menuMapper, PermissionMapper permissionMapper) {
        this.menuMapper = menuMapper;
        this.permissionMapper = permissionMapper;
    }

    @Autowired
    public void setPermissionMenuMapper(PermissionMenuMapper permissionMenuMapper) {
        this.permissionMenuMapper = permissionMenuMapper;
    }

    @Autowired
    public void setStaffRoleMapper(StaffRoleMapper staffRoleMapper) {
        this.staffRoleMapper = staffRoleMapper;
    }

    @Autowired
    public void setRolePermissionMapper(RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public List<Menu> findMenuList(MenuVo menuVo) {
        List<Long> roleId = staffRoleMapper.selectList(new QueryWrapper<StaffRole>().eq("staff_id", StaffThreadLocal.get().getId())).stream().map(StaffRole::getRoleId).collect(Collectors.toList());
        List<Long> permissionId = rolePermissionMapper.selectList(new QueryWrapper<RolePermission>().in("role_id", roleId)).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        List<Long> menuId = permissionMenuMapper.selectList(new QueryWrapper<PermissionMenu>().in("permission_id", permissionId).eq("delete_status", 0)).stream().map(PermissionMenu::getMenuId).collect(Collectors.toList());

        List<Menu> menuList = menuMapper.selectList(new QueryWrapper<Menu>().in("id", menuId).eq("publish_status", 1).eq("delete_status", 0)).stream().distinct().collect(Collectors.toList());

        List<Long> collect = menuList.stream().map(Menu::getPid).distinct().collect(Collectors.toList());

        for (Long i : collect) {
            menuList.add(menuMapper.selectOne(new QueryWrapper<Menu>().eq("id", i)));
        }

        List<Menu> parentMenu = menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id", 0));
        List<Menu> pathMenu = menuMapper.selectList(new QueryWrapper<Menu>().eq("path", "/url/index"));

        List<Menu> list = new ArrayList<>(pathMenu);
        list.addAll(menuList);
        list.addAll(parentMenu);

        Page.remove();
        List<String> listString = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getHref() != null) {
                listString.add(menu.getHref());
            }
        }

        listString.add("/url/index");
        listString.add("/url/404");
        listString.add("/url/nopage");
        listString.add("/url/userpassword");
        listString.add("/url/usersetting");
        listString.add("/url/online");
        Page.set(listString);

        return list;
    }

    @Override
    public <T> Result<T> add(MenuParam menuParam) {
        int i = 0;
        switch (menuParam.getCheck()) {
            case "parentMenu" -> i = menuMapper.insert(new Menu().setHref(menuParam.getPath()).setDescription(menuParam.getDescription()).setPid(0L).setStatus(1).setDeleteStatus(0).setTitle(menuParam.getName()));
            case "orderMenu" -> i = menuMapper.insert(new Menu().setHref(menuParam.getPath()).setDescription(menuParam.getDescription()).setPid(Long.parseLong(menuParam.getParentMenu())).setStatus(1).setDeleteStatus(0).setTitle(menuParam.getName()));
            case "childMenu" -> i = menuMapper.insert(new Menu().setHref(menuParam.getPath()).setDescription(menuParam.getDescription()).setPid(Long.parseLong(menuParam.getOrderMenu())).setStatus(1).setDeleteStatus(0).setTitle(menuParam.getName()));
            case "interface" -> {
                Permission permission = new Permission();
                i = permissionMapper.insert(permission.setName(menuParam.getName()).setPath(menuParam.getPath()).setDescription(menuParam.getDescription()).setDeleteStatus(0));
                if (i == 1) {
                    i = permissionMenuMapper.insert(new PermissionMenu().setPermissionId(permission.getId()).setMenuId(Long.parseLong(menuParam.getChildMenu())).setDeleteStatus(0));
                }
            }
        }
        if (i == 1) {
            return Result.ok(BusinessCode.ADD_SUCCESS);
        } else {
            return Result.fail(BusinessCode.ADD_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(MenuParam menuParam) {
        int i;
        if (!menuParam.getIsPermission().equals("3")) {
            i = menuMapper.updateById(new Menu().setId(Long.parseLong(menuParam.getId())).setDeleteStatus(1));
        } else {
            i = permissionMapper.updateById(new Permission().setId(Long.parseLong(menuParam.getFalseId())).setDeleteStatus(1));
        }
        if (i == 1){
            return Result.ok(BusinessCode.DELETE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }

    @Override
    public <T> Result<T> on(MenuParam menuParam) {
        int i = menuMapper.updateById(new Menu().setId(Long.parseLong(menuParam.getId())).setStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(MenuParam menuParam) {
        int i = menuMapper.updateById(new Menu().setId(Long.parseLong(menuParam.getId())).setStatus(0));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> update(MenuParam menuParam) {
        if (menuParam.getIsPermission().equals("1") || menuParam.getIsPermission().equals("2")) {
            Menu menu = menuMapper.selectById(menuParam.getId());
            Menu selectOne = menuMapper.selectOne(new QueryWrapper<Menu>().eq("name", menuParam.getName())
                    .ne("name", menu.getTitle()));

            if (selectOne != null) {
                return Result.fail("昵称重复");
            }
        } else if (menuParam.getIsPermission().equals("3")) {
            Menu menu = menuMapper.selectById(menuParam.getId());
            Menu selectOne = menuMapper.selectOne(new QueryWrapper<Menu>().eq("name", menuParam.getName())
                    .ne("name", menu.getTitle()));

            if (selectOne != null) {
                return Result.fail("昵称重复");
            }

            Menu one = menuMapper.selectOne(new QueryWrapper<Menu>().eq("path", menuParam.getPath())
                    .ne("path", menu.getHref()));

            if (one != null) {
                return Result.fail("路径重复");
            }
        } else {
            Permission permission = permissionMapper.selectById(menuParam.getId());

            Permission selectOne = permissionMapper.selectOne(new QueryWrapper<Permission>().eq("name", menuParam.getName())
                    .ne("name", permission.getName()));

            if (selectOne != null) {
                return Result.fail("昵称重复");
            }

            Permission one = permissionMapper.selectOne(new QueryWrapper<Permission>().eq("path", menuParam.getPath())
                    .ne("path", permission.getPath()));

            if (one != null) {
                return Result.fail("路径重复");
            }
        }


        int i = 0;
        switch (menuParam.getIsPermission()) {
            case "0" -> i = menuMapper.updateById(new Menu().setId(Long.parseLong(menuParam.getId())).setTitle(menuParam.getName()));
            case "1" -> i = menuMapper.updateById(new Menu().setId(Long.parseLong(menuParam.getId())).setPid(Long.parseLong(menuParam.getParentMenu())).setTitle(menuParam.getName()));
            case "2" -> i = menuMapper.updateById(new Menu().setId(Long.parseLong(menuParam.getId())).setHref(menuParam.getPath()).setPid(Long.parseLong(menuParam.getOrderMenu())).setTitle(menuParam.getName()));
            case "3" -> {
                permissionMapper.updateById(new Permission().setId(Long.parseLong(menuParam.getId())).setPath(menuParam.getPath()).setName(menuParam.getName()));
                i = permissionMenuMapper.update(new PermissionMenu().setMenuId(Long.parseLong(menuParam.getChildMenu())), new QueryWrapper<PermissionMenu>().eq("permission_id", menuParam.getId()));
            }
        }
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public Result<String> getParentMenuId(Long id) {
        Menu menu = menuMapper.selectOne(new QueryWrapper<Menu>().eq("id", id));
        Menu parentId = menuMapper.selectOne(new QueryWrapper<Menu>().eq("id", menu.getPid()));
        if (parentId != null) {
            return Result.ok(parentId.getId().toString());
        } else {
            return Result.fail("错误");
        }
    }

    @Override
    public Result<String> getIInterFaceMenuId(Long id) {
        Menu menu = menuMapper.selectOne(new QueryWrapper<Menu>().eq("id", id));
        Menu parentId = menuMapper.selectOne(new QueryWrapper<Menu>().eq("id", menu.getPid()));
        Menu interfaceId = menuMapper.selectOne(new QueryWrapper<Menu>().eq("id", parentId.getPid()));
        if (interfaceId != null) {
            return Result.ok(interfaceId.getId().toString());
        } else {
            return Result.fail("错误");
        }
    }
}
