package com.a243.magicbazaar.util.convert.impl;


import com.a243.magicbazaar.dao.entity.UserStars;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.OrderInfoMapper;
import com.a243.magicbazaar.dao.mapper.OrderMapper;
import com.a243.magicbazaar.dao.mapper.UserMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.UserStarsVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class UserStarsConvert implements Convert<UserStars, UserStarsVo> {
    private OrderInfoMapper orderInfoMapper;
    private OrderMapper orderMapper;
    private CommodityMapper commodityMapper;
    private UserMapper userMapper;

    @Autowired
    public void setOrderInfoMapper(OrderInfoMapper orderInfoMapper) {
        this.orderInfoMapper = orderInfoMapper;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public abstract UserStarsVo convert(UserStars userStars);

    public abstract List<UserStarsVo> convert(List<UserStars> userStarsList);

    @AfterMapping
    public void convert(UserStars userStars, @MappingTarget UserStarsVo userStarsVo) {
        Long orderId = orderInfoMapper.selectById(userStars.getOrderInfoId()).getOrderId();
        Long userId = orderMapper.selectById(orderId).getUserId();
        userStarsVo.setUsername(userMapper.selectById(userId).getNickname());

        Long commodityId = orderInfoMapper.selectById(userStars.getOrderInfoId()).getCommodityId();
        userStarsVo.setCommodityName(commodityMapper.selectById(commodityId).getTitle());
        userStarsVo.setUserPhoto(userMapper.selectById(userId).getAvatar());
    }

    @AfterMapping
    public void convert(List<UserStars> userStarsList, @MappingTarget List<UserStarsVo> userStarsVoList) {
        for (int i = 0; i < userStarsList.size(); i++) {
            convert(userStarsList.get(i), userStarsVoList.get(i));
        }
    }
}
