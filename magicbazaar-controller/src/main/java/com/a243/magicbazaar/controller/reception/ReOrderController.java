package com.a243.magicbazaar.controller.reception;

import com.a243.magicbazaar.service.reception.ReOrderService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.OrderParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeCommentVo;
import com.a243.magicbazaar.view.vo.OrderVo;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "reception/order")
public class ReOrderController {
    private final ReOrderService reOrderService;

    @Autowired
    public ReOrderController(ReOrderService reOrderService) {
        this.reOrderService = reOrderService;
    }

    @RequestMapping(value = "list")
    public Result<List<OrderVo>> list(OrderParam orderParam) {
        return reOrderService.list(orderParam);
    }

    @PostMapping(value = "changeone")
    public <T> Result<T> changeOne(OrderParam orderParam) {
        return reOrderService.changeOne(orderParam);
    }

    @PostMapping(value = "changeall")
    public <T> Result<T> changeAll(OrderParam orderParam) {
        return reOrderService.changeAll(orderParam);
    }

    @PostMapping(value = "dispute/info")
    public Result<List<CommodityDisputeCommentVo>> disputeInfo(OrderParam orderParam) {
        return reOrderService.disputeInfo(orderParam);
    }

    @PostMapping(value = "exitall")
    public <T> Result<T> exitAll(OrderParam orderParam) {
        return reOrderService.exitAll(orderParam);
    }

    @PostMapping(value = "exitone")
    public <T> Result<T> exitOne(OrderParam orderParam) {
        return reOrderService.exitOne(orderParam);
    }

    @PostMapping(value = "haveone")
    public <T> Result<T> haveOne(OrderParam orderParam) {
        return reOrderService.haveOne(orderParam);
    }

    @PostMapping(value = "haveall")
    public <T> Result<T> haveAll(OrderParam orderParam) {
        return reOrderService.haveAll(orderParam);
    }

    @PostMapping(value = "rate")
    public <T> Result<T> rate(OrderParam orderParam) {
        return reOrderService.rate(orderParam);
    }

    @PostMapping(value = "allorder")
    public Result<String> allOrder() {
        return reOrderService.allOrder();
    }

    @PostMapping(value = "allprice")
    public Result<String> allPrice() {
        return reOrderService.allPrice();
    }

    @PostMapping(value = "alldate")
    public Result<String> allDate() {
        return reOrderService.allDate();
    }

    @PostMapping(value = "closepaypal")
    public <T> Result<T> closePaypal(OrderParam orderParam) {
        return reOrderService.closePaypal(orderParam);
    }

    @PostMapping(value = "deleteone")
    public <T> Result<T> deleteOne(OrderParam orderParam) {
        return reOrderService.deleteOne(orderParam);
    }

    @PostMapping(value = "deleteall")
    public <T> Result<T> deleteAll(OrderParam orderParam) {
        return reOrderService.deleteAll(orderParam);
    }

    @PostMapping(value = "gosuccess")
    public <T> Result<T> goSuccess(OrderParam orderParam) {
        return reOrderService.goSuccess(orderParam);
    }
}
