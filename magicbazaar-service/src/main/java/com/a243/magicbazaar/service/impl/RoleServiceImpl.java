package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.dto.ChoosePermission;
import com.a243.magicbazaar.dao.entity.*;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.service.RoleService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.RoleConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.RoleParam;
import com.a243.magicbazaar.view.vo.RoleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;
    private final RoleConvert roleConvert;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper, RoleConvert roleConvert) {
        this.roleMapper = roleMapper;
        this.roleConvert = roleConvert;
    }

    private StaffRoleMapper staffRoleMapper;
    private MenuMapper menuMapper;
    private PermissionMenuMapper permissionMenuMapper;
    private PermissionMapper permissionMapper;
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    public void setStaffRoleMapper(StaffRoleMapper staffRoleMapper) {
        this.staffRoleMapper = staffRoleMapper;
    }

    @Autowired
    public void setMenuMapper(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Autowired
    public void setPermissionMenuMapper(PermissionMenuMapper permissionMenuMapper) {
        this.permissionMenuMapper = permissionMenuMapper;
    }

    @Autowired
    public void setPermissionMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Autowired
    public void setRolePermissionMapper(RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public Result<List<RoleVo>> list(RoleParam roleParam) {
        if (roleParam.getPageParam() != null) {
            IPage<Role> iPage = roleMapper.selectPage(new Page<>(roleParam.getPageParam().getPage(), roleParam.getPageParam().getPageSize()), new QueryWrapper<Role>()
                    .like(!StrUtil.isBlank(roleParam.getName()), "name", roleParam.getName())
                    .eq(!StrUtil.isBlank(roleParam.getPublishStatus()), "publish_status", roleParam.getPublishStatus())
                    .eq(!StrUtil.isBlank(roleParam.getDeleteStatus()), "delete_status", roleParam.getDeleteStatus()));

            return Result.ok(Math.toIntExact(iPage.getTotal()), roleConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(roleConvert.convert(roleMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> on(RoleParam roleParam) {
        int i = roleMapper.updateById(new Role().setId(Long.parseLong(roleParam.getId())).setPublishStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(RoleParam roleParam) {
        int i = roleMapper.updateById(new Role().setId(Long.parseLong(roleParam.getId())).setPublishStatus(0));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(RoleParam roleParam) {
        List<StaffRole> staffRoleList = staffRoleMapper.selectList(new QueryWrapper<StaffRole>().eq("role_id", roleParam.getId()));
        if (staffRoleList.size() == 0) {
            int i = roleMapper.updateById(new Role().setId(Long.parseLong(roleParam.getId())).setDeleteStatus(1));
            if (i == 1) {
                return Result.ok(BusinessCode.DELETE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.DELETE_ERROR);
            }
        } else {
            return Result.fail("???????????????????????????????????????");
        }
    }

    @Override
    public <T> Result<T> add(Role role) {
        Role role1 = roleMapper.selectOne(new QueryWrapper<Role>().eq("name", role.getName()));
        if (role1 != null) {
            return Result.fail("????????????");
        }

        int i = roleMapper.insert(role.setPublishStatus(1).setDeleteStatus(0));
        if (i == 1) {
            return Result.ok(BusinessCode.ADD_SUCCESS);
        } else {
            return Result.fail(BusinessCode.ADD_ERROR);
        }
    }

    @Override
    public <T> Result<T> update(Role role) {
        Role selectById = roleMapper.selectById(role.getId());
        Role selectOne = roleMapper.selectOne(new QueryWrapper<Role>().eq("name", role.getName())
                .ne("name", selectById.getName()));

        if (selectOne != null) {
            return Result.fail("????????????");
        }

        int i = roleMapper.updateById(role);
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public Result<List<ChoosePermission>> getPermission(RoleParam roleParam) {
        // ???????????????????????????
        List<Long> rolePermissionIdList = rolePermissionMapper.selectList(new QueryWrapper<RolePermission>().eq("role_id", roleParam.getId())).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        // ???????????????????????????
        List<Long> roleMenuIdList = new ArrayList<>();
        // ??????????????????????????????
        List<Long> roleMenuParentIdList = new ArrayList<>();
        // ????????????????????????????????????
        if (rolePermissionIdList.size() != 0) {
            roleMenuIdList = permissionMenuMapper.selectList(new QueryWrapper<PermissionMenu>().in("permission_id", rolePermissionIdList)).stream().map(PermissionMenu::getMenuId).distinct().collect(Collectors.toList());

            // ???????????????????????????????????????
            if (roleMenuIdList.size() != 0) {
                roleMenuParentIdList = menuMapper.selectList(new QueryWrapper<Menu>().in("id", roleMenuIdList)).stream().map(Menu::getPid).distinct().collect(Collectors.toList());
            }
        }

        // ?????? tree
        List<ChoosePermission> choosePermissionList = new ArrayList<>();

        // ?????????
        List<Menu> menuList = menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id", 1).isNull("path"));
        // ???????????????
        for (Menu menu : menuList) {
            ChoosePermission choosePermission = new ChoosePermission();

            // ??????????????????????????? ?????? ??????
            if (roleMenuParentIdList.size() != 0) {
                for (Long menuParentId : roleMenuParentIdList) {
                    if (menuParentId.equals(menu.getId())) {
//                        choosePermission.setChecked(true);
                        choosePermission.setSpread(true);
                    }
                }
            }

            // ?????? tree ???????????????
            choosePermissionList.add(choosePermission.setId(menu.getId()).setIsPermission(false).setTitle(menu.getTitle()).setField("menu"));
        }

        // ?????? tree ????????? ?????????????????????
        for (ChoosePermission choosePermission : choosePermissionList) {
            // ????????????id
            Long menuParentId = choosePermission.getId();
            // ?????????????????????
            List<Menu> menuChildrenList = menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id", menuParentId));
            // tree ????????? children
            List<ChoosePermission> choosePermissionChildren = new ArrayList<>();

            // ???????????????
            for (Menu menu : menuChildrenList) {
                ChoosePermission choosePermissionChild = new ChoosePermission();

                // ??????????????????????????? ?????? ??????
                if (roleMenuIdList.size() != 0) {
                    for (Long roleMenuId : roleMenuIdList) {
                        if (menu.getId().equals(roleMenuId)) {
//                            choosePermissionChild.setChecked(true);
                            choosePermissionChild.setSpread(true);
                        }
                    }
                }

                // tree ??????????????????children
                choosePermissionChildren.add(choosePermissionChild.setId(menu.getId()).setIsPermission(false).setTitle(menu.getTitle()).setField("menuChildren"));
            }

            // ?????? tree???????????????????????? ????????????
            for (ChoosePermission choosePermissionChildrenOne : choosePermissionChildren) {
                // ????????????id
                List<Long> permissionIdList = permissionMenuMapper.selectList(new QueryWrapper<PermissionMenu>().eq("menu_id", choosePermissionChildrenOne.getId())).stream().map(PermissionMenu::getPermissionId).collect(Collectors.toList());

                // tree??????????????????children ??????
                List<ChoosePermission> choosePermissionChildrenPermission = new ArrayList<>();
                for (Long id : permissionIdList) {
                    // ????????????
                    Permission permission = permissionMapper.selectById(id);

                    ChoosePermission choosePermissionChild = new ChoosePermission();

                    // ???????????????????????? ??????
                    if (rolePermissionIdList.size() != 0) {
                        for (Long rolePermissionId : rolePermissionIdList) {
                            if (rolePermissionId.equals(permission.getId())) {
                                choosePermissionChild.setChecked(true);
                            }
                        }
                    }

                    choosePermissionChildrenPermission.add(choosePermissionChild.setId(permission.getId()).setIsPermission(true).setTitle(permission.getName()).setField("permission"));
                }

                // ?????? ?????????children ????????????
                choosePermissionChildrenOne.setChildren(choosePermissionChildrenPermission);
            }

            // ?????? ????????? ???????????????
            choosePermission.setChildren(choosePermissionChildren);
        }

        return Result.ok(choosePermissionList);
    }


    @Override
    public <T> Result<T> updateRole(Long roleValue, List<ChoosePermission> choosePermissionList) {
        // ?????????????????????
        List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(new QueryWrapper<RolePermission>().eq("role_id", roleValue));

        int i = 0;
        // ????????????
        for (RolePermission rolePermission : rolePermissionList) {
            i += rolePermissionMapper.deleteById(rolePermission);
        }

        if (i == rolePermissionList.size()) {
            // ?????????????????????
            for (ChoosePermission choosePermission : choosePermissionList) {
                // ?????????
                if (choosePermission.getChildren() != null) {
                    for (ChoosePermission choosePermissionChild : choosePermission.getChildren()) {
                        // ??????
                        if (choosePermissionChild.getChildren() != null) {
                            // ??????????????????
                            for (ChoosePermission choosePermissionChildren : choosePermissionChild.getChildren()) {
                                // ??????????????????
                                rolePermissionMapper.insert(new RolePermission().setRoleId(roleValue).setPermissionId(choosePermissionChildren.getId()).setDeleteStatus(0));
                            }
                        }
                    }
                }
            }
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }
}
