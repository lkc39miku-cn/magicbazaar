package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Order;
import com.a243.magicbazaar.dao.entity.OrderType;
import com.a243.magicbazaar.dao.mapper.OrderMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.util.thread.UserThreadLocal;
import com.a243.magicbazaar.view.vo.OrderTypeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class OrderTypeConvert implements Convert<OrderType, OrderTypeVo> {
    private OrderMapper orderMapper;

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public abstract OrderTypeVo convert(OrderType orderType);

    public abstract List<OrderTypeVo> convert(List<OrderType> orderTypeList);

    @AfterMapping
    public void convert(OrderType orderType, @MappingTarget OrderTypeVo orderTypeVo) {
        if (UserThreadLocal.get() != null) {
            orderTypeVo.setNumber(orderMapper.selectCount(new QueryWrapper<Order>()
                    .eq("order_type_id", orderType.getId())
                    .eq("user_id", UserThreadLocal.get().getId())
                    .eq("publish_status", 1)
                    .eq("delete_status", 0)).toString());
        }
    }

    @AfterMapping
    public void convert(List<OrderType> orderTypeList, @MappingTarget List<OrderTypeVo> orderTypeVoList) {
        for (int i = 0; i < orderTypeList.size(); i++) {
            convert(orderTypeList.get(i), orderTypeVoList.get(i));
        }
    }
}
