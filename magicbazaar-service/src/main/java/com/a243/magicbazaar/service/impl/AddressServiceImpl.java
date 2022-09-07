package com.a243.magicbazaar.service.impl;

import com.a243.magicbazaar.dao.entity.Address;
import com.a243.magicbazaar.dao.mapper.AddressMapper;
import com.a243.magicbazaar.service.AddressService;
import com.a243.magicbazaar.util.convert.impl.AddressConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.AddressParam;
import com.a243.magicbazaar.view.vo.AddressVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    private final AddressMapper addressMapper;
    private final AddressConvert addressConvert;

    @Autowired
    public AddressServiceImpl(AddressMapper addressMapper, AddressConvert addressConvert) {
        this.addressMapper = addressMapper;
        this.addressConvert = addressConvert;
    }

    @Override
    public Result<List<AddressVo>> list(AddressParam addressParam) {
        if (addressParam.getTree()) {
            List<Address> addresses = addressMapper.selectList(new QueryWrapper<>());
            return Result.ok(addresses.size(), addressConvert.convert(addresses));
        } else {
            return Result.ok(addressConvert.convert(addressMapper.selectList(new QueryWrapper<>())));
        }
    }
}
