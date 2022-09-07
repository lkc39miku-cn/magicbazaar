package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.CommodityDisputeService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityDisputeParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeVo;
import com.alipay.api.AlipayApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/commodity/dispute")
public class CommodityDisputeController {
    private final CommodityDisputeService commodityDisputeService;

    @Autowired
    public CommodityDisputeController(CommodityDisputeService commodityDisputeService) {
        this.commodityDisputeService = commodityDisputeService;
    }

    @PostMapping(value = "list")
    public Result<List<CommodityDisputeVo>> list(CommodityDisputeParam commodityDisputeParam) {
        return commodityDisputeService.list(commodityDisputeParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(CommodityDisputeParam commodityDisputeParam) {
        return commodityDisputeService.on(commodityDisputeParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(CommodityDisputeParam commodityDisputeParam) {
        return commodityDisputeService.off(commodityDisputeParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(CommodityDisputeParam commodityDisputeParam) {
        return commodityDisputeService.delete(commodityDisputeParam);
    }

    @PostMapping(value = "change")
    public <T> Result<T> change(CommodityDisputeParam commodityDisputeParam) {
        return commodityDisputeService.change(commodityDisputeParam);
    }

    @PostMapping(value = "exit")
    public <T> Result<T> exit(CommodityDisputeParam commodityDisputeParam) throws AlipayApiException {
        return commodityDisputeService.exit(commodityDisputeParam);
    }

    @PostMapping(value = "changeone")
    public <T> Result<T> changeOne(CommodityDisputeParam commodityDisputeParam) {
        return commodityDisputeService.changeOne(commodityDisputeParam);
    }

    @PostMapping(value = "exitone")
    public <T> Result<T> exitOne(CommodityDisputeParam commodityDisputeParam) throws AlipayApiException {
        return commodityDisputeService.exitOne(commodityDisputeParam);
    }

    @PostMapping(value = "counts")
    public Result<String> disputeCounts() {
        return commodityDisputeService.disputeCounts();
    }
}
