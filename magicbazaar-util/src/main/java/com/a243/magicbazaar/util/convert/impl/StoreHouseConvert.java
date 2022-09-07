package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.StoreHouse;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.StoreHouseVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class StoreHouseConvert implements Convert<StoreHouse, StoreHouseVo> {
    private CommodityMapper commodityMapper;

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    public abstract StoreHouseVo convert(StoreHouse storeHouse);

    public abstract List<StoreHouseVo> convert(List<StoreHouse> storeHouseList);

    @AfterMapping
    public void convert(StoreHouse storeHouse, @MappingTarget StoreHouseVo storeHouseVo) {
        storeHouseVo.setCommodityName(commodityMapper.selectById(storeHouse.getCommodityId()).getTitle());
    }

    @AfterMapping
    public void convert(List<StoreHouse> storeHouseList, @MappingTarget List<StoreHouseVo> storeHouseVoList) {
        for (int i = 0; i < storeHouseList.size(); i++) {
            convert(storeHouseList.get(i), storeHouseVoList.get(i));
        }
    }
}
