package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.CommodityDisputeInfo;
import com.a243.magicbazaar.dao.entity.OrderInfo;
import com.a243.magicbazaar.dao.mapper.CommodityDisputeMapper;
import com.a243.magicbazaar.dao.mapper.OrderInfoMapper;
import com.a243.magicbazaar.dao.mapper.OrderMapper;
import com.a243.magicbazaar.dao.mapper.OrderTypeMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.CommodityDisputeInfoVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class CommodityDisputeInfoConvert implements Convert<CommodityDisputeInfo, CommodityDisputeInfoVo> {
    private CommodityDisputeMapper commodityDisputeMapper;
    private OrderInfoMapper orderInfoMapper;
    private OrderMapper orderMapper;
    private OrderTypeMapper orderTypeMapper;

    @Autowired
    public void setCommodityDisputeMapper(CommodityDisputeMapper commodityDisputeMapper) {
        this.commodityDisputeMapper = commodityDisputeMapper;
    }

    @Autowired
    public void setOrderInfoMapper(OrderInfoMapper orderInfoMapper) {
        this.orderInfoMapper = orderInfoMapper;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setOrderTypeMapper(OrderTypeMapper orderTypeMapper) {
        this.orderTypeMapper = orderTypeMapper;
    }

    public abstract CommodityDisputeInfoVo convert(CommodityDisputeInfo commodityDisputeInfo);
    public abstract List<CommodityDisputeInfoVo> convert(List<CommodityDisputeInfo> commodityDisputeInfoList);

    @AfterMapping
    public void convert(CommodityDisputeInfo commodityDisputeInfo, @MappingTarget CommodityDisputeInfoVo commodityDisputeInfoVo) {
        commodityDisputeInfoVo.setCommodityDisputeNumber(commodityDisputeMapper.selectById(commodityDisputeInfo.getCommodityDisputeId()).getDisputeNumber());
        commodityDisputeInfoVo.setOrderInfoNumber(orderMapper.selectById(orderInfoMapper.selectById(commodityDisputeInfo.getOrderInfoId()).getOrderId()).getOrderNumber());
        commodityDisputeInfoVo.setOrderTypeId(orderInfoMapper.selectById(commodityDisputeInfo.getOrderInfoId()).getOrderTypeId());
        commodityDisputeInfoVo.setOrderTypeName(orderTypeMapper.selectById(orderInfoMapper.selectById(commodityDisputeInfo.getOrderInfoId()).getOrderTypeId()).getName());
        OrderInfo orderInfo = orderInfoMapper.selectById(commodityDisputeInfo.getOrderInfoId());
        if (orderInfo.getOrderTypeId() == 7 || orderInfo.getOrderTypeId() == 11 || orderInfo.getOrderTypeId() == 13) {
            commodityDisputeInfoVo.setPrice(orderInfo.getAllPrice());
        }
    }

    @AfterMapping
    public void convert(List<CommodityDisputeInfo> commodityDisputeInfoList, @MappingTarget List<CommodityDisputeInfoVo> commodityDisputeInfoVoList) {
        for (int i = 0; i < commodityDisputeInfoList.size(); i++) {
            convert(commodityDisputeInfoList.get(i), commodityDisputeInfoVoList.get(i));
        }
    }
}
