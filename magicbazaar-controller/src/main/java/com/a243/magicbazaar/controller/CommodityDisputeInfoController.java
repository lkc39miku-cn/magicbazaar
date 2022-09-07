package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.CommodityDisputeInfoService;
import com.a243.magicbazaar.service.CommodityDisputeService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityDisputeInfoParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/commodity/dispute/info")
public class CommodityDisputeInfoController {
    private final CommodityDisputeInfoService commodityDisputeInfoService;

    @Autowired
    public CommodityDisputeInfoController(CommodityDisputeInfoService commodityDisputeInfoService) {
        this.commodityDisputeInfoService = commodityDisputeInfoService;
    }

    @PostMapping(value = "list")
    public Result<List<CommodityDisputeInfoVo>> list(CommodityDisputeInfoParam commodityDisputeInfoParam) {
        return commodityDisputeInfoService.list(commodityDisputeInfoParam);
    }
}
