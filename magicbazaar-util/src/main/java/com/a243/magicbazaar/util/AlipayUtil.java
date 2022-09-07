package com.a243.magicbazaar.util;

import com.a243.magicbazaar.util.result.Result;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.Cleanup;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AlipayUtil {

    // 获得初始化的AlipayClient
    private final static AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APP_ID,
            AlipayConfig.ALIPAY_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);

    // 下单支付页面
    public static AlipayTradePagePayResponse createOrder(String outTradeNo, String totalAmount, String subject, String body, HttpServletResponse response) throws Exception {
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.RETURN_URL);
        alipayRequest.setNotifyUrl(AlipayConfig.NOTIFY_URL);
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        //订单编号
        model.setOutTradeNo(outTradeNo);
        //销售产品码
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        //订单总金额
        model.setTotalAmount(totalAmount);
        //订单标题
        model.setSubject(subject);
        //订单描述
        model.setBody(body);
        alipayRequest.setBizModel(model);

        //请求
        String result;
        AlipayTradePagePayResponse alipayTradePagePayResponse = null;

        try {
            alipayTradePagePayResponse = alipayClient.pageExecute(alipayRequest);
            if (alipayTradePagePayResponse.isSuccess()){
                System.out.println("创建订单成功!");
            }else {
                System.out.println("创建订单失败!");
            }

            result = alipayTradePagePayResponse.getBody();
            response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);

            @Cleanup PrintWriter printWriter = response.getWriter();
            printWriter.write(result);
        } catch (AlipayApiException | IOException e) {
            e.printStackTrace();
        }
        return alipayTradePagePayResponse;
    }

    // 查询支付
    public static String findOrder(String outTradeNo) throws AlipayApiException {
        AlipayTradeQueryRequest request =  new AlipayTradeQueryRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        //订单编号
        model.setOutTradeNo(outTradeNo);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()){
            System.out.println( "交易查询调用成功!");
        }  else {
            System.out.println("交易查询调用失败!");
        }
        System.out.println(response.getBody());
        return response.getBody();
    }

    // 退款
    public static <T> Result<T> exit(String orderNumber, String money) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();

        bizContent.put("out_trade_no", orderNumber);
        bizContent.put("refund_amount", money);
        bizContent.put("out_request_no", String.valueOf(System.currentTimeMillis()));

        request.setBizContent(bizContent.toString());

        AlipayTradeRefundResponse response = alipayClient.execute(request);

        System.out.println(response.getFundChange());
        System.out.println(response.getRefundFee());

        if(response.isSuccess()){

            System.out.println("调用成功");
            return Result.ok();
        } else {
            System.out.println("调用失败");
            return Result.fail("退款失败");
        }
    }

    // 查询付款
    public static String findPay(String out_trade_no) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", out_trade_no);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return null;
    }

    // 查询退款
        public static String findExit(String outTradeNo) throws AlipayApiException {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("trade_no", "2021081722001419121412730660");
        bizContent.put("out_request_no", "HZ01RF001");

        request.setBizContent(bizContent.toString());
        AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return null;
    }
}