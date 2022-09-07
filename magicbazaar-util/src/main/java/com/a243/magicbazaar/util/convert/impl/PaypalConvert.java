package com.a243.magicbazaar.util.convert.impl;


import com.a243.magicbazaar.dao.entity.Paypal;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.PaypalVo;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class PaypalConvert implements Convert<Paypal, PaypalVo> {
    public abstract PaypalVo convert(Paypal paypal);

    public abstract List<PaypalVo> convert(List<Paypal> paypalList);
}
