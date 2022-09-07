package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.StoreHouseOut;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.dao.mapper.StoreHouseMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.StoreHouseOutVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class StoreHouseOutConvert implements Convert<StoreHouseOut, StoreHouseOutVo> {
    private StaffMapper staffMapper;
    private StoreHouseMapper storeHouseMapper;
    private CommodityMapper commodityMapper;

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    @Autowired
    public void setStoreHouseMapper(StoreHouseMapper storeHouseMapper) {
        this.storeHouseMapper = storeHouseMapper;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    public abstract StoreHouseOutVo convert(StoreHouseOut storeHouseOut);

    public abstract List<StoreHouseOutVo> convert(List<StoreHouseOut> storeHouseOutList);

    @AfterMapping
    public void convert(StoreHouseOut storeHouseOut, @MappingTarget StoreHouseOutVo storeHouseOutVo) {
        if (storeHouseOut.getStaffId() != null) {
            storeHouseOutVo.setStaffName(staffMapper.selectById(storeHouseOut.getStaffId()).getUsername());
        }
        storeHouseOutVo.setTargetName(staffMapper.selectById(storeHouseOut.getTargetId()).getUsername());
        storeHouseOutVo.setCommodityName(commodityMapper.selectById(storeHouseMapper.selectById(storeHouseOut.getStoreHouseId()).getCommodityId()).getTitle());
    }

    @AfterMapping
    public void convert(List<StoreHouseOut> storeHouseOutList, @MappingTarget List<StoreHouseOutVo> storeHouseOutVoList) {
        for (int i = 0; i < storeHouseOutList.size(); i++) {
            convert(storeHouseOutList.get(i), storeHouseOutVoList.get(i));
        }
    }
}
