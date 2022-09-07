package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.CommodityVerifyService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityVerifyParam;
import com.a243.magicbazaar.view.vo.CommodityVerifyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/commodity/verify")
public class CommodityVerifyController {
    private final CommodityVerifyService commodityVerifyService;

    @Autowired
    public CommodityVerifyController(CommodityVerifyService commodityVerifyService) {
        this.commodityVerifyService = commodityVerifyService;
    }

    @PostMapping(value = "list")
    public Result<List<CommodityVerifyVo>> list(CommodityVerifyParam commodityVerifyParam) {
        return commodityVerifyService.list(commodityVerifyParam);
    }

    @PostMapping(value = "success")
    public <T> Result<T> success(CommodityVerifyParam commodityVerifyParam) {
        return commodityVerifyService.success(commodityVerifyParam);
    }

    @PostMapping(value = "fail")
    public <T> Result<T> fail(CommodityVerifyParam commodityVerifyParam) {
        return commodityVerifyService.fail(commodityVerifyParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(CommodityVerifyParam commodityVerifyParam) {
        return commodityVerifyService.delete(commodityVerifyParam);
    }
}
