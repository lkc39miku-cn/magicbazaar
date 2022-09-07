package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "admin/paypal")
public class PaypalController {
    private final PaypalService paypalService;

    @Autowired
    public PaypalController(PaypalService paypalService) {
        this.paypalService = paypalService;
    }
}
