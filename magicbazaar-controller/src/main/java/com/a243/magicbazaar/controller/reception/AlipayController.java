package com.a243.magicbazaar.controller.reception;

import com.a243.magicbazaar.service.reception.AlipayService;
import com.a243.magicbazaar.util.result.Result;
import com.alipay.api.AlipayApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "reception/alipay")
public class AlipayController {

    private final AlipayService alipayService;

    @Autowired
    public AlipayController(AlipayService alipayService) {
        this.alipayService = alipayService;
    }

    @RequestMapping(value = "pay")
    public void alipay(String idList, String totalAmount, String addressId, HttpServletResponse response) throws Exception {
        alipayService.alipay(idList, totalAmount, addressId, response);
    }

    @RequestMapping(value = "repay")
    public void reAlipay(String orderId, HttpServletResponse response) throws Exception {
        alipayService.reAlipay(orderId, response);
    }

    @GetMapping(value = "return")
    public String returnUrl(HttpServletRequest request) throws AlipayApiException {
        alipayService.returnUrl(request);
        return "redirect:/reception/url/index";
    }

    @RequestMapping("notify")
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException {
        alipayService.notifyUrl(request, response);
    }
}
