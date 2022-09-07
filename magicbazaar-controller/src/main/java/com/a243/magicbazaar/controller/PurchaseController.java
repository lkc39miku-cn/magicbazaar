package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.Purchase;
import com.a243.magicbazaar.service.PurchaseService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.PurchaseParam;
import com.a243.magicbazaar.view.vo.PurchaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping(value = "list")
    public Result<List<PurchaseVo>> list(PurchaseParam purchaseParam) {
        return purchaseService.list(purchaseParam);
    }

    @PostMapping(value = "add")
    public <T> Result<T> add(Purchase purchase) {
        return purchaseService.add(purchase);
    }

    @PostMapping(value = "update")
    public <T> Result<T> update(Purchase purchase) {
        return purchaseService.update(purchase);
    }

    @PostMapping(value = "again")
    public <T> Result<T> again(PurchaseParam purchaseParam) {
        return purchaseService.again(purchaseParam);
    }

    @PostMapping(value = "reupdate")
    public <T> Result<T> reUpdate(Purchase purchase) {
        return purchaseService.reUpdate(purchase);
    }

    @PostMapping(value = "info")
    public Result<String> info(PurchaseParam purchaseParam) {
        return purchaseService.info(purchaseParam);
    }

    @PostMapping(value = "yes")
    public <T> Result<T> yes(PurchaseParam purchaseParam) {
        return purchaseService.yes(purchaseParam);
    }

    @PostMapping(value = "no")
    public <T> Result<T> no(PurchaseParam purchaseParam) {
        return purchaseService.no(purchaseParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(PurchaseParam purchaseParam) {
        return purchaseService.delete(purchaseParam);
    }
}
