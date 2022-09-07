package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.dao.entity.CommodityBrand;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.CommodityBrandVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class CommodityBrandConvert implements Convert<CommodityBrand, CommodityBrandVo> {
    private CommodityMapper commodityMapper;

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    public abstract CommodityBrandVo convert(CommodityBrand commodityBrand);

    public abstract List<CommodityBrandVo> convert(List<CommodityBrand> commodityBrandList);

    @AfterMapping
    public void convert(CommodityBrand commodityBrand, @MappingTarget CommodityBrandVo commodityBrandVo) {
        commodityBrandVo.setNumber(commodityMapper.selectCount(new QueryWrapper<Commodity>()
                .eq("publish_status", 1)
                .eq("delete_status", 0)
                .eq("commodity_brand_id", commodityBrand.getId())).toString());
    }

    @AfterMapping
    public void convert(List<CommodityBrand> commodityBrandList, @MappingTarget List<CommodityBrandVo> commodityBrandVoList) {
        for (int i = 0; i < commodityBrandList.size(); i++) {
            convert(commodityBrandList.get(i), commodityBrandVoList.get(i));
        }
    }
}
