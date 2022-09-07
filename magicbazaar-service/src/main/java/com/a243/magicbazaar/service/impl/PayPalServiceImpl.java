package com.a243.magicbazaar.service.impl;

import com.a243.magicbazaar.dao.mapper.PaypalMapper;
import com.a243.magicbazaar.service.PaypalService;
import com.a243.magicbazaar.util.convert.impl.PaypalConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PayPalServiceImpl implements PaypalService {
    private final PaypalMapper paypalMapper;
    private final PaypalConvert paypalConvert;

    @Autowired
    public PayPalServiceImpl(PaypalMapper paypalMapper, PaypalConvert paypalConvert) {
        this.paypalMapper = paypalMapper;
        this.paypalConvert = paypalConvert;
    }
}
