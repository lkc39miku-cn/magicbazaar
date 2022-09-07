package com.a243.magicbazaar.controller.reception;

import com.a243.magicbazaar.service.reception.ReCommodityBrandService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityBrandParam;
import com.a243.magicbazaar.view.vo.CommodityBrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "reception/commodity/brand")
public class ReCommodityBrandController {
    private final ReCommodityBrandService reCommodityBrandService;

    @Autowired
    public ReCommodityBrandController(ReCommodityBrandService reCommodityBrandService) {
        this.reCommodityBrandService = reCommodityBrandService;
    }

    @PostMapping(value = "list")
    public Result<List<CommodityBrandVo>> list(CommodityBrandParam commodityBrandParam) {
        return reCommodityBrandService.list(commodityBrandParam);
    }
}
