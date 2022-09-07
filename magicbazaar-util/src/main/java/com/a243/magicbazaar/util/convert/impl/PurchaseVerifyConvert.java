package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.PurchaseVerify;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.PurchaseMapper;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.PurchaseVerifyVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class PurchaseVerifyConvert implements Convert<PurchaseVerify, PurchaseVerifyVo> {
    private StaffMapper staffMapper;
    private PurchaseMapper purchaseMapper;
    private CommodityMapper commodityMapper;

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    @Autowired
    public void setPurchaseMapper(PurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    public abstract PurchaseVerifyVo convert(PurchaseVerify purchaseVerify);

    public abstract List<PurchaseVerifyVo> convert(List<PurchaseVerify> purchaseVerifyList);

    @AfterMapping
    public void convert(PurchaseVerify purchaseVerify, @MappingTarget PurchaseVerifyVo purchaseVerifyVo) {
        purchaseVerifyVo.setCommodityName(commodityMapper.selectById(purchaseMapper.selectById(purchaseVerify.getPurchaseId()).getCommodityId()).getTitle());
        if (purchaseVerify.getStaffId() != null) {
            purchaseVerifyVo.setStaffName(staffMapper.selectById(purchaseVerify.getStaffId()).getUsername());
        }
        purchaseVerifyVo.setTargetName(staffMapper.selectById(purchaseVerify.getTargetId()).getUsername());
    }

    @AfterMapping
    public void convert(List<PurchaseVerify> purchaseVerifyList, @MappingTarget List<PurchaseVerifyVo> purchaseVerifyVoList) {
        for (int i = 0; i < purchaseVerifyList.size(); i++) {
            convert(purchaseVerifyList.get(i), purchaseVerifyVoList.get(i));
        }
    }
}
