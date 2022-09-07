package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.dto.Cart;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.UserMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.CartVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class CartConvert implements Convert<Cart, CartVo> {
    private UserMapper userMapper;
    private UserConvert userConvert;
    private CommodityMapper commodityMapper;
    private CommodityConvert commodityConvert;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserConvert(UserConvert userConvert) {
        this.userConvert = userConvert;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setCommodityConvert(CommodityConvert commodityConvert) {
        this.commodityConvert = commodityConvert;
    }

    public abstract CartVo convert(Cart cart);
    public abstract List<CartVo> convert(List<Cart> cartList);

    @AfterMapping
    public void convert(Cart cart, @MappingTarget CartVo cartVo) {
        cartVo.setUserVo(userConvert.convert(userMapper.selectById(cart.getUserId())));
        cartVo.setCommodityVo(commodityConvert.convert(commodityMapper.selectById(cart.getCommodityId())));
    }

    @AfterMapping
    public void convert(List<Cart> cartList, @MappingTarget List<CartVo> cartVoList) {
        for (int i = 0; i < cartList.size(); i++) {
            convert(cartList.get(i), cartVoList.get(i));
        }
    }
}
