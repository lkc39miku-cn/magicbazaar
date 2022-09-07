package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.StoreHouseOut;
import com.a243.magicbazaar.service.StoreHouseOutService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.StoreHouseOutParam;
import com.a243.magicbazaar.view.vo.StoreHouseOutVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/store/house/out")
public class StoreHouseOutController {
    private final StoreHouseOutService storeHouseOutService;

    @Autowired
    public StoreHouseOutController(StoreHouseOutService storeHouseOutService) {
        this.storeHouseOutService = storeHouseOutService;
    }

    @PostMapping(value = "list")
    public Result<List<StoreHouseOutVo>> list(StoreHouseOutParam storeHouseOutParam) {
        return storeHouseOutService.list(storeHouseOutParam);
    }

    @PostMapping(value = "out")
    public <T> Result<T> out(StoreHouseOut storeHouseOut) {
        return storeHouseOutService.out(storeHouseOut);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(StoreHouseOutParam storeHouseOutParam) {
        return storeHouseOutService.delete(storeHouseOutParam);
    }
}
