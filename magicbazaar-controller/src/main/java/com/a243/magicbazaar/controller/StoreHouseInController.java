package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.StoreHouseIn;
import com.a243.magicbazaar.service.StoreHouseInService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.StoreHouseInParam;
import com.a243.magicbazaar.view.vo.StoreHouseInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/store/house/in")
public class StoreHouseInController {
    private final StoreHouseInService storeHouseInService;

    @Autowired
    public StoreHouseInController(StoreHouseInService storeHouseInService) {
        this.storeHouseInService = storeHouseInService;
    }

    @PostMapping(value = "list")
    public Result<List<StoreHouseInVo>> list(StoreHouseInParam storeHouseInParam) {
        return storeHouseInService.list(storeHouseInParam);
    }

    @PostMapping(value = "update")
    public <T> Result<T> update(StoreHouseIn storeHouseIn) {
        return storeHouseInService.update(storeHouseIn);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(StoreHouseInParam storeHouseInParam) {
        return storeHouseInService.delete(storeHouseInParam);
    }
}
