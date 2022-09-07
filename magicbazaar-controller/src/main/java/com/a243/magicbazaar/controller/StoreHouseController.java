package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.StoreHouse;
import com.a243.magicbazaar.service.StoreHouseService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.StoreHouseParam;
import com.a243.magicbazaar.view.vo.StoreHouseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/store/house")
public class StoreHouseController {
    private final StoreHouseService storeHouseService;

    @Autowired
    public StoreHouseController(StoreHouseService storeHouseService) {
        this.storeHouseService = storeHouseService;
    }

    @PostMapping(value = "list")
    public Result<List<StoreHouseVo>> list(StoreHouseParam storeHouseParam) {
        return storeHouseService.list(storeHouseParam);
    }

    @PostMapping(value = "out")
    public <T> Result<T> out(StoreHouseParam storeHouseParam) {
        return storeHouseService.out(storeHouseParam);
    }

    @PostMapping(value = "update")
    public <T> Result<T> update(StoreHouse storeHouse) {
        return storeHouseService.update(storeHouse);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(StoreHouseParam storeHouseParam) {
        return storeHouseService.delete(storeHouseParam);
    }
}
