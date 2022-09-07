package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Address;
import com.a243.magicbazaar.dao.entity.UserAddress;
import com.a243.magicbazaar.dao.mapper.AddressMapper;
import com.a243.magicbazaar.dao.mapper.UserMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.UserAddressVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class UserAddressConvert implements Convert<UserAddress, UserAddressVo> {
    private AddressMapper addressMapper;
    private UserMapper userMapper;

    @Autowired
    public void setAddressMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public abstract UserAddressVo convert(UserAddress userAddress);
    public abstract List<UserAddressVo> convert(List<UserAddress> userAddressList);

    @AfterMapping
    public void convert(UserAddress userAddress, @MappingTarget UserAddressVo userAddressVo) {
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

        userAddressVo.setAddressName(addressInfo);
        userAddressVo.setUserName(userMapper.selectById(userAddress.getUserId()).getNickname());
    }

    @AfterMapping
    public void convert(List<UserAddress> userAddressList, @MappingTarget List<UserAddressVo> userAddressVoList) {
        for (int i = 0; i < userAddressList.size(); i++) {
            convert(userAddressList.get(i),userAddressVoList.get(i));
        }
    }
}
