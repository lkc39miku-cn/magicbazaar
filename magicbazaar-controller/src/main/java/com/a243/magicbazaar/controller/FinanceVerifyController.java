package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.FinanceVerifyService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.FinanceVerifyParam;
import com.a243.magicbazaar.view.vo.FinanceVerifyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/finance/verify")
public class FinanceVerifyController {
    private final FinanceVerifyService financeVerifyService;

    @Autowired
    public FinanceVerifyController(FinanceVerifyService financeVerifyService) {
        this.financeVerifyService = financeVerifyService;
    }

    @PostMapping(value = "list")
    public Result<List<FinanceVerifyVo>> list(FinanceVerifyParam financeVerifyParam) {
        return financeVerifyService.list(financeVerifyParam);
    }

    @PostMapping(value = "success")
    public <T> Result<T> success(FinanceVerifyParam financeVerifyParam) {
        return financeVerifyService.success(financeVerifyParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(FinanceVerifyParam financeVerifyParam) {
        return financeVerifyService.delete(financeVerifyParam);
    }

    @PostMapping(value = "fail")
    public <T> Result<T> fail(FinanceVerifyParam financeVerifyParam) {
        return financeVerifyService.fail(financeVerifyParam);
    }
}
