package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.StaffVo;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class StaffConvert implements Convert<Staff, StaffVo> {
    public abstract StaffVo convert(Staff staff);

    public abstract List<StaffVo> convert(List<Staff> staffList);
}
