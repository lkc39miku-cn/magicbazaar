package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.OrderTypeService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.OrderTypeParam;
import com.a243.magicbazaar.view.vo.OrderTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/order/type")
public class OrderTypeController {
    private final OrderTypeService orderTypeService;

    @Autowired
    public OrderTypeController(OrderTypeService orderTypeService) {
        this.orderTypeService = orderTypeService;
    }

    @PostMapping(value = "list")
    public Result<List<OrderTypeVo>> list(OrderTypeParam orderTypeParam) {
        return orderTypeService.list(orderTypeParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(OrderTypeParam orderTypeParam) {
        return orderTypeService.on(orderTypeParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(OrderTypeParam orderTypeParam) {
        return orderTypeService.off(orderTypeParam);
    }
}
