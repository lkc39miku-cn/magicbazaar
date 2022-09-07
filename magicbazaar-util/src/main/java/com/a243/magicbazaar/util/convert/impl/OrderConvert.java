package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Order;
import com.a243.magicbazaar.dao.entity.OrderInfo;
import com.a243.magicbazaar.dao.mapper.OrderInfoMapper;
import com.a243.magicbazaar.dao.mapper.OrderTypeMapper;
import com.a243.magicbazaar.dao.mapper.PaypalMapper;
import com.a243.magicbazaar.dao.mapper.UserMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.OrderVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class OrderConvert implements Convert<Order, OrderVo> {
    private UserMapper userMapper;
    private PaypalMapper paypalMapper;
    private OrderTypeMapper orderTypeMapper;
    private OrderInfoMapper orderInfoMapper;
    private OrderInfoConvert orderInfoConvert;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setPaypalMapper(PaypalMapper paypalMapper) {
        this.paypalMapper = paypalMapper;
    }

    @Autowired
    public void setOrderTypeMapper(OrderTypeMapper orderTypeMapper) {
        this.orderTypeMapper = orderTypeMapper;
    }

    @Autowired
    public void setOrderInfoMapper(OrderInfoMapper orderInfoMapper) {
        this.orderInfoMapper = orderInfoMapper;
    }

    @Autowired
    public void setOrderInfoConvert(OrderInfoConvert orderInfoConvert) {
        this.orderInfoConvert = orderInfoConvert;
    }

    public abstract OrderVo convert(Order order);

    public abstract List<OrderVo> convert(List<Order> orderList);

    @AfterMapping
    public void convert(Order order, @MappingTarget OrderVo orderVo) {
        orderVo.setUserName(userMapper.selectById(order.getUserId()).getNickname());
        orderVo.setPaypalName(paypalMapper.selectById(order.getPaypalId()).getName());
        orderVo.setOrderTypeName(orderTypeMapper.selectById(order.getOrderTypeId()).getName());
        orderVo.setChild(orderInfoConvert.convert(orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId())
        .eq("publish_status", 1))));

        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId())
        .eq("publish_status", 1));

        boolean check = true;
        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfo.getOrderTypeId() == 1) {
                check = false;
                break;
            }
        }
        orderVo.setCheck(check);
    }

    @AfterMapping
    public void convert(List<Order> orderList, @MappingTarget List<OrderVo> orderVoList) {
        for (int i = 0; i < orderList.size(); i++) {
            convert(orderList.get(i), orderVoList.get(i));
        }
    }
}
