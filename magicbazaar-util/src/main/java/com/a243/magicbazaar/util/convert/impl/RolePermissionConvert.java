package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.RolePermission;
import com.a243.magicbazaar.dao.mapper.PermissionMapper;
import com.a243.magicbazaar.dao.mapper.RoleMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.RolePermissionVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class RolePermissionConvert implements Convert<RolePermission, RolePermissionVo> {
    private RoleMapper roleMapper;
    private PermissionMapper permissionMapper;

    @Autowired
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Autowired
    public void setPermissionMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public abstract RolePermissionVo convert(RolePermission rolePermission);

    public abstract List<RolePermissionVo> convert(List<RolePermission> rolePermissionList);

    @AfterMapping
    public void convert(RolePermission rolePermission, @MappingTarget RolePermissionVo rolePermissionVo) {
        rolePermissionVo.setRoleName(roleMapper.selectById(rolePermission.getRoleId()).getName());
        rolePermissionVo.setPermissionName(permissionMapper.selectById(rolePermission.getPermissionId()).getName());
    }

    @AfterMapping
    public void convert(List<RolePermission> rolePermissionList, @MappingTarget List<RolePermissionVo> rolePermissionVoList) {
        for (int i = 0; i < rolePermissionList.size(); i++) {
            convert(rolePermissionList.get(i), rolePermissionVoList.get(i));
        }
    }
}
