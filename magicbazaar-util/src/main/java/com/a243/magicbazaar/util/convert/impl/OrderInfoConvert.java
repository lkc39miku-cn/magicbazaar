package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Address;
import com.a243.magicbazaar.dao.entity.OrderInfo;
import com.a243.magicbazaar.dao.entity.UserAddress;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.OrderInfoVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Component
public abstract class OrderInfoConvert implements Convert<OrderInfo, OrderInfoVo> {
    private CommodityMapper commodityMapper;
    private OrderTypeMapper orderTypeMapper;
    private UserAddressMapper userAddressMapper;
    private AddressMapper addressMapper;
    private UserMapper userMapper;

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setOrderTypeMapper(OrderTypeMapper orderTypeMapper) {
        this.orderTypeMapper = orderTypeMapper;
    }

    @Autowired
    public void setUserAddressMapper(UserAddressMapper userAddressMapper) {
        this.userAddressMapper = userAddressMapper;
    }

    @Autowired
    public void setAddressMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public abstract OrderInfoVo convert(OrderInfo orderInfo);

    public abstract List<OrderInfoVo> convert(List<OrderInfo> orderInfoList);

    @AfterMapping
    public void convert(OrderInfo orderInfo, @MappingTarget OrderInfoVo orderInfoVo) {
        orderInfoVo.setCommodityName(commodityMapper.selectById(orderInfo.getCommodityId()).getTitle());
        orderInfoVo.setOrderTypeName(orderTypeMapper.selectById(orderInfo.getOrderTypeId()).getName());
        orderInfoVo.setCommodityPhoto(commodityMapper.selectById(orderInfo.getCommodityId()).getPhoto());
        UserAddress userAddress = userAddressMapper.selectById(orderInfo.getUserAddressId());
        orderInfoVo.setUserPhone(userAddress.getPhone());
        orderInfoVo.setUserName(userMapper.selectById(userAddress.getUserId()).getNickname());
        Address address = addressMapper.selectById(userAddress.getAddressId());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(address.getName()).append(";");
        while (address.getParentId() != 0) {
            address = addressMapper.selectById(address.getParentId());
            stringBuilder.append(address.getName()).append(" ").append(";");
        }

        List<String> list = Arrays.stream(stringBuilder.toString().split(";")).toList();

        List<String> returnList = new ArrayList<>();
        for (int i = list.size(); i > 0; i--) {
            returnList.add(list.get(i - 1));
        }

        String addressInfo = String.join("", returnList);

        addressInfo += " " + userAddress.getAddressInfo();

        orderInfoVo.setUserAddressName(addressInfo);
    }

    @AfterMapping
    public void convert(List<OrderInfo> orderInfoList, @MappingTarget List<OrderInfoVo> orderInfoVoList) {
        for (int i = 0; i < orderInfoList.size(); i++) {
            convert(orderInfoList.get(i), orderInfoVoList.get(i));
        }
    }
}
