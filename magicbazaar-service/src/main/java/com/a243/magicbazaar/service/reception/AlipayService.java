package com.a243.magicbazaar.service.reception;

import com.alipay.api.AlipayApiException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AlipayService {
    /**
     * 支付
     * @param totalAmount
     * @param response
     */
    void alipay(String idList, String totalAmount, String addressId, HttpServletResponse response) throws Exception ;

    /**
     * 支付成功
     * @param request
     */
    void returnUrl(HttpServletRequest request) throws AlipayApiException;

    /**
     * 检查支付单数据
     * @param request
     * @param response
     */
    @Deprecated
    void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException ;

    /**
     * 继续支付
     * @param orderId
     * @param response
     */
    void reAlipay(String orderId, HttpServletResponse response) throws Exception;
}
