package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.FinanceVerify;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.PurchaseMapper;
import com.a243.magicbazaar.dao.mapper.PurchaseVerifyMapper;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.FinanceVerifyVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class FinanceVerifyConvert implements Convert<FinanceVerify, FinanceVerifyVo> {
    private CommodityMapper commodityMapper;
    private PurchaseMapper purchaseMapper;
    private PurchaseVerifyMapper purchaseVerifyMapper;
    private StaffMapper staffMapper;


    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setPurchaseMapper(PurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }

    @Autowired
    public void setPurchaseVerifyMapper(PurchaseVerifyMapper purchaseVerifyMapper) {
        this.purchaseVerifyMapper = purchaseVerifyMapper;
    }

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    public abstract FinanceVerifyVo convert(FinanceVerify financeVerify);

    public abstract List<FinanceVerifyVo> convert(List<FinanceVerify> financeVerifyList);


    @AfterMapping
    public void convert(FinanceVerify financeVerify, @MappingTarget FinanceVerifyVo financeVerifyVo) {
        financeVerifyVo.setCommodityName(commodityMapper.selectById(purchaseMapper.selectById(purchaseVerifyMapper.selectById(financeVerify.getPurchaseVerifyId()).getPurchaseId()).getCommodityId()).getTitle());
        if (financeVerify.getStaffId() != null) {
            financeVerifyVo.setStaffName(staffMapper.selectById(financeVerify.getStaffId()).getUsername());
        }
        financeVerifyVo.setTargetName(staffMapper.selectById(financeVerify.getTargetId()).getUsername());
    }

    @AfterMapping
    public void convert(List<FinanceVerify> financeVerifyList, @MappingTarget List<FinanceVerifyVo> financeVerifyVoList) {
        for (int i = 0; i < financeVerifyList.size(); i++) {
            convert(financeVerifyList.get(i), financeVerifyVoList.get(i));
        }
    }
}
