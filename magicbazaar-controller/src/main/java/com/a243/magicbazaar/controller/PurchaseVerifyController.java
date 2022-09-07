package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.PurchaseVerifyService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.PurchaseVerifyParam;
import com.a243.magicbazaar.view.vo.PurchaseVerifyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/purchase/verify")
public class PurchaseVerifyController {

    private final PurchaseVerifyService purchaseVerifyService;

    @Autowired
    public PurchaseVerifyController(PurchaseVerifyService purchaseVerifyService) {
        this.purchaseVerifyService = purchaseVerifyService;
    }

    @PostMapping(value = "list")
    public Result<List<PurchaseVerifyVo>> list(PurchaseVerifyParam purchaseVerifyParam) {
        return purchaseVerifyService.list(purchaseVerifyParam);
    }

    @PostMapping(value = "success")
    public <T> Result<T> success(PurchaseVerifyParam purchaseVerifyParam) {
        return purchaseVerifyService.success(purchaseVerifyParam);
    }

    @PostMapping(value = "fail")
    public <T> Result<T> fail(PurchaseVerifyParam purchaseVerifyParam) {
        return purchaseVerifyService.fail(purchaseVerifyParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(PurchaseVerifyParam purchaseVerifyParam) {
        return purchaseVerifyService.delete(purchaseVerifyParam);
    }
}
