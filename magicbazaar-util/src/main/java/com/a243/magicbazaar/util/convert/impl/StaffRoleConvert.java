package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.StaffRole;
import com.a243.magicbazaar.dao.mapper.RoleMapper;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.StaffRoleVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class StaffRoleConvert implements Convert<StaffRole, StaffRoleVo> {
    private StaffMapper staffMapper;
    private RoleMapper roleMapper;

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    @Autowired
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public abstract StaffRoleVo convert(StaffRole staffRole);

    public abstract List<StaffRoleVo> convert(List<StaffRole> staffRoleList);

    @AfterMapping
    public void convert(StaffRole staffRole, @MappingTarget StaffRoleVo staffRoleVo) {
        staffRoleVo.setStaffName(staffMapper.selectById(staffRole.getStaffId()).getUsername());
        staffRoleVo.setRoleName(roleMapper.selectById(staffRole.getRoleId()).getName());
    }

    @AfterMapping
    public void convert(List<StaffRole> staffRoleList, @MappingTarget List<StaffRoleVo> staffRoleVoList) {
        for (int i = 0; i < staffRoleList.size(); i++) {
            convert(staffRoleList.get(i), staffRoleVoList.get(i));
        }
    }
}
