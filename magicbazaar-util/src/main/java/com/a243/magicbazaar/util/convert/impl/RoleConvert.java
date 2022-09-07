package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Role;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.RoleVo;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class RoleConvert implements Convert<Role, RoleVo> {
    public abstract RoleVo convert(Role role);

    public abstract List<RoleVo> convert(List<Role> roleList);
}
