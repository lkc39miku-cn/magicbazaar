package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.service.CommodityService;
import com.a243.magicbazaar.util.FileUploadUtil;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityParam;
import com.a243.magicbazaar.view.vo.CommodityVerifyVo;
import com.a243.magicbazaar.view.vo.CommodityVo;
import com.a243.magicbazaar.view.vo.StoreHouseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "admin/commodity")
public class CommodityController {

    private final CommodityService commodityService;

    @Autowired
    public CommodityController(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @RequestMapping(value = "list")
    public Result<List<CommodityVo>> list(CommodityParam commodityParam) {
        return commodityService.list(commodityParam);
    }

    @RequestMapping(value = "upload")
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        map.put("savePath", FileUploadUtil.upload(file));
        return map;
    }

    @PostMapping(value = "add")
    public <T> Result<T> add(Commodity commodity) {
        return commodityService.add(commodity);
    }

    @PostMapping(value = "update")
    public <T> Result<T> update(Commodity commodity) {
        return commodityService.update(commodity);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(CommodityParam commodityParam) {
        return commodityService.delete(commodityParam);
    }

    @PostMapping(value = "addnum")
    public <T> Result<T> addNum(CommodityParam commodityParam) {
        return commodityService.addNum(commodityParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(CommodityParam commodityParam) {
        return commodityService.off(commodityParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(CommodityParam commodityParam) {
        return commodityService.on(commodityParam);
    }

    @PostMapping(value = "storenum")
    public Result<StoreHouseVo> storeNum(CommodityParam commodityParam) {
        return commodityService.storeNum(commodityParam);
    }

    @PostMapping(value = "failinfo")
    public Result<CommodityVerifyVo> failInfo(CommodityParam commodityParam) {
        return commodityService.failInfo(commodityParam);
    }

    @PostMapping(value = "again")
    public <T> Result<T> again(CommodityParam commodityParam) {
        return commodityService.again(commodityParam);
    }

    @PostMapping(value = "checkname")
    public <T> Result<T> checkName(CommodityParam commodityParam) {
        return commodityService.checkName(commodityParam);
    }

    @PostMapping(value = "checktime")
    public <T> Result<T> checkTime(CommodityParam commodityParam) {
        return commodityService.checkTime(commodityParam);
    }

    @PostMapping(value = "getmoney")
    public Result<String> getMoney(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        return commodityService.getMoney(startTime, endTime);
    }

    @PostMapping(value = "outmoney")
    public Result<String> outMoney(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        return commodityService.outMoney(startTime, endTime);
    }
}
