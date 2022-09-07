package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.dao.entity.CommodityBrand;
import com.a243.magicbazaar.dao.entity.CommodityType;
import com.a243.magicbazaar.dao.entity.CommodityVerify;
import com.a243.magicbazaar.dao.mapper.CommodityBrandMapper;
import com.a243.magicbazaar.dao.mapper.CommodityTypeMapper;
import com.a243.magicbazaar.dao.mapper.CommodityVerifyMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.CommodityVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class CommodityConvert implements Convert<Commodity, CommodityVo> {
    private CommodityTypeMapper commodityTypeMapper;
    private CommodityBrandMapper commodityBrandMapper;
    private CommodityVerifyMapper commodityVerifyMapper;

    @Autowired
    public void setCommodityTypeMapper(CommodityTypeMapper commodityTypeMapper) {
        this.commodityTypeMapper = commodityTypeMapper;
    }

    @Autowired
    public void setCommodityBrandMapper(CommodityBrandMapper commodityBrandMapper) {
        this.commodityBrandMapper = commodityBrandMapper;
    }

    @Autowired
    public void setCommodityVerifyMapper(CommodityVerifyMapper commodityVerifyMapper) {
        this.commodityVerifyMapper = commodityVerifyMapper;
    }

    public abstract CommodityVo convert(Commodity commodity);

    public abstract List<CommodityVo> convert(List<Commodity> commodityList);

    @AfterMapping
    public void convert(Commodity commodity, @MappingTarget CommodityVo commodityVo) {
        commodityVo.setCommodityBrandName(commodityBrandMapper.selectOne(new QueryWrapper<CommodityBrand>().eq("id", commodity.getCommodityBrandId())).getName());
        commodityVo.setCommodityTypeName(commodityTypeMapper.selectOne(new QueryWrapper<CommodityType>().eq("id", commodity.getCommodityTypeId())).getName());
        commodityVo.setCommodityVerify(commodityVerifyMapper.selectOne(new QueryWrapper<CommodityVerify>().eq("commodity_id", commodity.getId())).getVerifyStatus());
    }

    @AfterMapping
    public void convert(List<Commodity> commodityList, @MappingTarget List<CommodityVo> commodityVoList) {
        for (int i = 0; i < commodityList.size(); i++) {
            convert(commodityList.get(i), commodityVoList.get(i));
        }
    }
}
