package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.StoreHouseIn;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.PurchaseMapper;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.StoreHouseInVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class StoreHouseInConvert implements Convert<StoreHouseIn, StoreHouseInVo> {
    private PurchaseMapper purchaseMapper;
    private CommodityMapper commodityMapper;
    private StaffMapper staffMapper;

    @Autowired
    public void setPurchaseMapper(PurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    public abstract StoreHouseInVo convert(StoreHouseIn storeHouseIn);

    public abstract List<StoreHouseInVo> convert(List<StoreHouseIn> storeHouseInList);

    @AfterMapping
    public void convert(StoreHouseIn storeHouseIn, @MappingTarget StoreHouseInVo storeHouseInVo) {
        storeHouseInVo.setCommodityName(commodityMapper.selectById(purchaseMapper.selectById(storeHouseIn.getPurchaseId()).getCommodityId()).getTitle());
        storeHouseInVo.setTargetName(staffMapper.selectById(storeHouseIn.getTargetId()).getUsername());
        if (storeHouseIn.getStaffId() != null) {
            storeHouseInVo.setStaffName(staffMapper.selectById(storeHouseIn.getStaffId()).getUsername());
        }
    }

    @AfterMapping
    public void convert(List<StoreHouseIn> storeHouseInList, @MappingTarget List<StoreHouseInVo> storeHouseInVoList) {
        for (int i = 0; i < storeHouseInList.size(); i++) {
            convert(storeHouseInList.get(i), storeHouseInVoList.get(i));
        }
    }
}
