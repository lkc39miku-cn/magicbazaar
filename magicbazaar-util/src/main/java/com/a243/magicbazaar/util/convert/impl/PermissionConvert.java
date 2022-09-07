package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Permission;
import com.a243.magicbazaar.dao.entity.PermissionMenu;
import com.a243.magicbazaar.dao.mapper.PermissionMenuMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.PermissionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class PermissionConvert implements Convert<Permission, PermissionVo> {
    private PermissionMenuMapper permissionMenuMapper;

    @Autowired
    public void setPermissionMenuMapper(PermissionMenuMapper permissionMenuMapper) {
        this.permissionMenuMapper = permissionMenuMapper;
    }

    public abstract PermissionVo convert(Permission permission);

    public abstract List<PermissionVo> convert(List<Permission> permissionList);

    @AfterMapping
    public void convert(Permission permission, @MappingTarget PermissionVo permissionVo) {
        permissionVo.setMenuId(permissionMenuMapper.selectOne(new QueryWrapper<PermissionMenu>().eq("permission_id", permission.getId())).getMenuId());
    }

    @AfterMapping
    public void convert(List<Permission> permissionList, @MappingTarget List<PermissionVo> permissionVoList) {
        for (int i = 0; i < permissionList.size(); i++) {
            convert(permissionList.get(i), permissionVoList.get(i));
        }
    }
}
