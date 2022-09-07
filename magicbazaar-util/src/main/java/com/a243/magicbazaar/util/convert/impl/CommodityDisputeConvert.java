package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.CommodityDispute;
import com.a243.magicbazaar.dao.entity.Order;
import com.a243.magicbazaar.dao.entity.OrderInfo;
import com.a243.magicbazaar.dao.mapper.OrderInfoMapper;
import com.a243.magicbazaar.dao.mapper.OrderMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.CommodityDisputeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class CommodityDisputeConvert implements Convert<CommodityDispute, CommodityDisputeVo> {
    private OrderMapper orderMapper;
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setOrderInfoMapper(OrderInfoMapper orderInfoMapper) {
        this.orderInfoMapper = orderInfoMapper;
    }

    public abstract CommodityDisputeVo convert(CommodityDispute commodityDispute);

    public abstract List<CommodityDisputeVo> convert(List<CommodityDispute> commodityDisputeList);

    @AfterMapping
    public void convert(CommodityDispute commodityDispute, @MappingTarget CommodityDisputeVo commodityDisputeVo) {
        commodityDisputeVo.setOrderNumber(orderMapper.selectById(commodityDispute.getOrderId()).getOrderNumber());
    }

    @AfterMapping
    public void convert(List<CommodityDispute> commodityDisputeList, @MappingTarget List<CommodityDisputeVo> commodityDisputeVoList) {
        for (int i = 0; i < commodityDisputeList.size(); i++) {
            convert(commodityDisputeList.get(i), commodityDisputeVoList.get(i));
        }
    }
}
