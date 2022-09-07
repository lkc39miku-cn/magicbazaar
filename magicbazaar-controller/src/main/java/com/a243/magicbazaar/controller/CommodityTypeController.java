package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.CommodityType;
import com.a243.magicbazaar.service.CommodityTypeService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityTypeParam;
import com.a243.magicbazaar.view.vo.CommodityTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/commodity/type")
public class CommodityTypeController {

    private final CommodityTypeService commodityTypeService;

    @Autowired
    public CommodityTypeController(CommodityTypeService commodityTypeService) {
        this.commodityTypeService = commodityTypeService;
    }

    @RequestMapping(value = "list")
    public Result<List<CommodityTypeVo>> list(CommodityTypeParam commodityTypeParam) {
        return commodityTypeService.list(commodityTypeParam);
    }

    @PostMapping(value = "one")
    public Result<CommodityTypeVo> one(CommodityTypeParam commodityTypeParam) {
        return commodityTypeService.one(commodityTypeParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(CommodityTypeParam commodityTypeParam) {
        return commodityTypeService.delete(commodityTypeParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(CommodityTypeParam commodityTypeParam) {
        return commodityTypeService.on(commodityTypeParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(CommodityTypeParam commodityTypeParam) {
        return commodityTypeService.off(commodityTypeParam);
    }

    @PostMapping(value = "add")
    public <T> Result<T> add(CommodityType commodityType) {
        return commodityTypeService.add(commodityType);
    }

    @PostMapping(value = "update")
    public <T> Result<T> update(CommodityType commodityType) {
        return commodityTypeService.update(commodityType);
    }

    @PostMapping(value = "child")
    public Result<List<CommodityTypeVo>> child(CommodityTypeParam commodityTypeParam) {
        return commodityTypeService.child(commodityTypeParam);
    }
}
