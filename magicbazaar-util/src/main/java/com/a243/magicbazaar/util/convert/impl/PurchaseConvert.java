package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.FinanceVerify;
import com.a243.magicbazaar.dao.entity.Purchase;
import com.a243.magicbazaar.dao.entity.PurchaseVerify;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.FinanceVerifyMapper;
import com.a243.magicbazaar.dao.mapper.PurchaseVerifyMapper;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.view.vo.PurchaseVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class PurchaseConvert {
    private CommodityMapper commodityMapper;
    private PurchaseVerifyMapper purchaseVerifyMapper;
    private FinanceVerifyMapper financeVerifyMapper;
    private StaffMapper staffMapper;

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setPurchaseVerifyMapper(PurchaseVerifyMapper purchaseVerifyMapper) {
        this.purchaseVerifyMapper = purchaseVerifyMapper;
    }

    @Autowired
    public void setFinanceVerifyMapper(FinanceVerifyMapper financeVerifyMapper) {
        this.financeVerifyMapper = financeVerifyMapper;
    }

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    public abstract PurchaseVo convert(Purchase purchase);

    public abstract List<PurchaseVo> convert(List<Purchase> purchaseList);

    @AfterMapping
    public void convert(Purchase purchase, @MappingTarget PurchaseVo purchaseVo) {
        purchaseVo.setCommodityName(commodityMapper.selectById(purchase.getCommodityId()).getTitle());
        purchaseVo.setStaffName(staffMapper.selectById(purchase.getStaffId()).getUsername());
        if (purchaseVerifyMapper.selectOne(new QueryWrapper<PurchaseVerify>().eq("purchase_id", purchase.getId())).getStaffId() != null) {
            purchaseVo.setPurchaseName(staffMapper.selectById(purchaseVerifyMapper.selectOne(new QueryWrapper<PurchaseVerify>().eq("purchase_id", purchase.getId())).getStaffId()).getUsername());
            purchaseVo.setPurchaseOk(String.valueOf(purchaseVerifyMapper.selectOne(new QueryWrapper<PurchaseVerify>().eq("purchase_id", purchase.getId())).getVerifyStatus()));
        }
        if (financeVerifyMapper.selectOne(new QueryWrapper<FinanceVerify>().eq("purchase_verify_id", purchaseVerifyMapper.selectOne(new QueryWrapper<PurchaseVerify>().eq("purchase_id", purchase.getId())).getId())).getStaffId() != null) {
            purchaseVo.setFinanceName(staffMapper.selectById(financeVerifyMapper.selectOne(new QueryWrapper<FinanceVerify>().eq("purchase_verify_id", purchaseVerifyMapper.selectOne(new QueryWrapper<PurchaseVerify>().eq("purchase_id", purchase.getId())).getId())).getStaffId()).getUsername());
            purchaseVo.setFinanceOk(String.valueOf(financeVerifyMapper.selectOne(new QueryWrapper<FinanceVerify>().eq("purchase_verify_id", purchaseVerifyMapper.selectOne(new QueryWrapper<PurchaseVerify>().eq("purchase_id", purchase.getId())).getId())).getVerifyStatus()));
        }
    }

    @AfterMapping
    public void convert(List<Purchase> purchaseList, @MappingTarget List<PurchaseVo> purchaseVoList) {
        for (int i = 0; i < purchaseList.size(); i++) {
            convert(purchaseList.get(i), purchaseVoList.get(i));
        }
    }
}
