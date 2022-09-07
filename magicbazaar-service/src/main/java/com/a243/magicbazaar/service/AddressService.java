package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.AddressParam;
import com.a243.magicbazaar.view.vo.AddressVo;

import java.util.List;

public interface AddressService {
    /**
     * 地址信息
     *
     * @param addressParam 参数
     * @return 结果集
     */
    Result<List<AddressVo>> list(AddressParam addressParam);
}
