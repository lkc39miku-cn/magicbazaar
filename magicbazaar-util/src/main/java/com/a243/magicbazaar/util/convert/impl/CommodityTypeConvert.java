package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.dao.entity.CommodityType;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.CommodityTypeMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.CommodityTypeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class CommodityTypeConvert implements Convert<CommodityType, CommodityTypeVo> {

    private CommodityMapper commodityMapper;
    private CommodityTypeMapper commodityTypeMapper;

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setCommodityTypeMapper(CommodityTypeMapper commodityTypeMapper) {
        this.commodityTypeMapper = commodityTypeMapper;
    }

    public abstract CommodityTypeVo convert(CommodityType commodityType);

    public abstract List<CommodityTypeVo> convert(List<CommodityType> commodityTypes);

    @AfterMapping
    public void convert(CommodityType commodityType, @MappingTarget CommodityTypeVo commodityTypeVo) {
        if (commodityType.getParentId() == 7) {
            commodityTypeVo.setChildren(convert(commodityTypeMapper.selectList(new QueryWrapper<CommodityType>().eq("parent_id", commodityType.getId()).eq("publish_status", 1))));

            for (CommodityTypeVo commodityTypeVoChild : commodityTypeVo.getChildren()) {
                commodityTypeVoChild.setNumber(commodityMapper.selectCount(new QueryWrapper<Commodity>()
                        .eq("publish_status", 1)
                        .eq("delete_status", 0)
                        .eq("commodity_type_id", commodityTypeVoChild.getId())).toString());
            }
        }
    }

    @AfterMapping
    public void convert(List<CommodityType> commodityTypeList, @MappingTarget List<CommodityTypeVo> commodityTypeVoList) {
        for (int i = 0 ; i < commodityTypeList.size() ;i++) {
            convert(commodityTypeList.get(i), commodityTypeVoList.get(i));
        }
    }

}
