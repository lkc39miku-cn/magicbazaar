package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.dao.entity.CommodityVerify;
import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.CommodityVerifyVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.aspectj.lang.annotation.AfterThrowing;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class CommodityVerifyConvert implements Convert<CommodityVerify, CommodityVerifyVo> {
    private CommodityMapper commodityMapper;
    private StaffMapper staffMapper;

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    public abstract CommodityVerifyVo convert(CommodityVerify commodityVerify);

    public abstract List<CommodityVerifyVo> convert(List<CommodityVerify> commodityVerifyList);

    @AfterThrowing
    public void convert(CommodityVerify commodityVerify, @MappingTarget CommodityVerifyVo commodityVerifyVo) {
        commodityVerifyVo.setCommodityName(commodityMapper.selectOne(new QueryWrapper<Commodity>().eq("id", commodityVerify.getCommodityId())).getTitle());
        if (commodityVerify.getStaffId() != null) {
            commodityVerifyVo.setStaffName(staffMapper.selectOne(new QueryWrapper<Staff>().eq("id", commodityVerify.getStaffId())).getUsername());
        }
        commodityVerifyVo.setTargetName(staffMapper.selectOne(new QueryWrapper<Staff>().eq("id", commodityVerify.getTargetId())).getUsername());
    }

    @AfterMapping
    public void convert(List<CommodityVerify> commodityVerifyList, @MappingTarget List<CommodityVerifyVo> commodityVerifyVoList) {
        for (int i = 0; i < commodityVerifyList.size(); i++) {
            convert(commodityVerifyList.get(i), commodityVerifyVoList.get(i));
        }
    }
}
