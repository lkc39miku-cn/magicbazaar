package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.CommodityBrand;
import com.a243.magicbazaar.service.CommodityBrandService;
import com.a243.magicbazaar.util.FileUploadUtil;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityBrandParam;
import com.a243.magicbazaar.view.vo.CommodityBrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "admin/commodity/brand")
public class CommodityBrandController {

    private final CommodityBrandService commodityBrandService;

    @Autowired
    public CommodityBrandController(CommodityBrandService commodityBrandService) {
        this.commodityBrandService = commodityBrandService;
    }

    @RequestMapping(value = "list")
    public Result<List<CommodityBrandVo>> list(CommodityBrandParam commodityBrandParam) {
        return commodityBrandService.list(commodityBrandParam);
    }

    @RequestMapping(value = "upload")
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        map.put("savePath", FileUploadUtil.upload(file));
        return map;
    }

    @PostMapping(value = "add")
    public <T> Result<T> add(CommodityBrand commodityBrand) {
        return commodityBrandService.add(commodityBrand);
    }

    @PostMapping(value = "update")
    public <T> Result<T> update(CommodityBrand commodityBrand) {
        return commodityBrandService.update(commodityBrand);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(CommodityBrandParam commodityBrandParam) {
        return commodityBrandService.delete(commodityBrandParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(CommodityBrandParam commodityBrandParam) {
        return commodityBrandService.on(commodityBrandParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(CommodityBrandParam commodityBrandParam) {
        return commodityBrandService.off(commodityBrandParam);
    }
}
