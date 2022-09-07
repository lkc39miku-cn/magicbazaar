package com.a243.magicbazaar.controller.reception;

import com.a243.magicbazaar.service.reception.ReCommodityTypeService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityTypeParam;
import com.a243.magicbazaar.view.vo.CommodityTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "reception/commodity/type")
public class ReCommodityTypeController {

    private final ReCommodityTypeService reCommodityTypeService;

    @Autowired
    public ReCommodityTypeController(ReCommodityTypeService reCommodityTypeService) {
        this.reCommodityTypeService = reCommodityTypeService;
    }

    @PostMapping(value = "list")
    public Result<List<CommodityTypeVo>> list(CommodityTypeParam commodityTypeParam) {
        return reCommodityTypeService.list(commodityTypeParam);
    }
}
