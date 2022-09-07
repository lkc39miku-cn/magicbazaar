package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Address;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.AddressVo;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class AddressConvert implements Convert<Address, AddressVo> {
    public abstract AddressVo convert(Address address);

    public abstract List<AddressVo> convert(List<Address> addressList);
}
