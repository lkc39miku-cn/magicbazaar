package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.OrderService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.OrderParam;
import com.a243.magicbazaar.view.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "list")
    public Result<List<OrderVo>> list(OrderParam orderParam) {
        return orderService.list(orderParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(OrderParam orderParam) {
        return orderService.on(orderParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(OrderParam orderParam) {
        return orderService.off(orderParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(OrderParam orderParam) {
        return orderService.delete(orderParam);
    }

    @PostMapping(value = "fire")
    public <T> Result<T> fire(OrderParam orderParam) {
        return orderService.fire(orderParam);
    }

    @PostMapping(value = "counts")
    public Result<String> orderCounts() {
        return orderService.orderCounts();
    }

    @PostMapping(value = "moneycounts")
    public Result<String> moneyCounts() {
        return orderService.moneyCounts();
    }
}
