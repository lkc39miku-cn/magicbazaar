package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.OrderInfoService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.OrderInfoParam;
import com.a243.magicbazaar.view.vo.OrderInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/order/info")
public class OrderInfoController {
    private final OrderInfoService orderInfoService;

    @Autowired
    public OrderInfoController(OrderInfoService orderInfoService) {
        this.orderInfoService = orderInfoService;
    }

    @PostMapping(value = "list")
    public Result<List<OrderInfoVo>> list(OrderInfoParam orderInfoParam) {
        return orderInfoService.list(orderInfoParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(OrderInfoParam orderInfoParam) {
        return orderInfoService.on(orderInfoParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(OrderInfoParam orderInfoParam) {
        return orderInfoService.off(orderInfoParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(OrderInfoParam orderInfoParam) {
        return orderInfoService.delete(orderInfoParam);
    }

    @PostMapping(value = "fire")
    public <T> Result<T> fire(OrderInfoParam orderInfoParam) {
        return orderInfoService.fire(orderInfoParam);
    }
}
