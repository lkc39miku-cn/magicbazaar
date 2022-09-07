package com.a243.magicbazaar.service.impl.reception;

import com.a243.magicbazaar.dao.dto.Cart;
import com.a243.magicbazaar.dao.entity.*;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.service.reception.AlipayService;
import com.a243.magicbazaar.util.AlipayConfig;
import com.a243.magicbazaar.util.AlipayUtil;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.UserThreadLocal;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AlipayServiceImpl implements AlipayService {
    private final OrderMapper orderMapper;
    private final OrderInfoMapper orderInfoMapper;
    private final CommodityDisputeMapper commodityDisputeMapper;
    private final CommodityDisputeCommentMapper commodityDisputeCommentMapper;

    @Autowired
    public AlipayServiceImpl(OrderMapper orderMapper, OrderInfoMapper orderInfoMapper, CommodityDisputeMapper commodityDisputeMapper, CommodityDisputeCommentMapper commodityDisputeCommentMapper) {
        this.orderMapper = orderMapper;
        this.orderInfoMapper = orderInfoMapper;
        this.commodityDisputeMapper = commodityDisputeMapper;
        this.commodityDisputeCommentMapper = commodityDisputeCommentMapper;
    }

    private CartMapper cartMapper;
    private CommodityMapper commodityMapper;

    @Autowired
    public void setCartMapper(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Override
    public void alipay(String idList, String totalAmount, String addressId, HttpServletResponse response) throws Exception {
        Long outTradeNo = System.currentTimeMillis();

        // 请求支付
        AlipayTradePagePayResponse tradePagePayResponse = AlipayUtil.createOrder(String.valueOf(outTradeNo), totalAmount, "magicbazaar", "测试", response);

        // 请求成功
        if (tradePagePayResponse.isSuccess()) {

            // 删除购物车物品
            List<String> strings = Arrays.stream(idList.split(",")).collect(Collectors.toList());

            //成功
            Order order = new Order()
                    .setOrderNumber(String.valueOf(outTradeNo))
                    .setUserId(UserThreadLocal.get().getId())
                    .setPaypalId(1L)
                    .setMoney(BigDecimal.valueOf(Long.parseLong(totalAmount)))
                    .setOrderTypeId(9L)
                    .setPublishStatus(1)
                    .setDeleteStatus(0);

            orderMapper.insert(order);

            // 订单详情
            for (String i : strings) {
                OrderInfo orderInfo = new OrderInfo()
                        .setOrderId(order.getId())
                        .setCommodityId(Long.parseLong(i));

                Commodity commodity = commodityMapper.selectOne(new QueryWrapper<Commodity>().eq("id", i));

                // 查看是否有折扣
                if (commodity.getPromotionPrice() != null) {
                    // 判断是否在日期中
                    if (commodity.getPromotionStartTime().isAfter(LocalDateTime.now()) && commodity.getPreviewEndTime().isBefore(LocalDate.now())) {
                        orderInfo.setPrice(commodity.getPromotionPrice());
                    } else {
                        orderInfo.setPrice(commodity.getPrice());
                    }
                } else {
                    orderInfo.setPrice(commodity.getPrice());
                }

                orderInfo.setNumber(cartMapper.selectOne(new QueryWrapper<Cart>().eq("user_id", UserThreadLocal.get().getId()).eq("commodity_id", i)).getCommodityNumber());

                orderInfo.setAllPrice(orderInfo.getPrice().multiply(BigDecimal.valueOf(orderInfo.getNumber())));
                orderInfo.setOrderTypeId(1L)
                .setPublishStatus(1)
                .setDeleteStatus(0)
                .setUserAddressId(Long.parseLong(addressId));

                orderInfoMapper.insert(orderInfo);
            }

            cartMapper.delete(new QueryWrapper<Cart>().eq("user_id", UserThreadLocal.get().getId()).in("commodity_id", strings));
        }
    }

    @Override
    public void reAlipay(String orderId, HttpServletResponse response) throws Exception {
        Order order = orderMapper.selectById(orderId);

        String outTradeNo = order.getOrderNumber();
        String totalAmount = String.valueOf(order.getMoney());

        AlipayTradePagePayResponse tradePagePayResponse = AlipayUtil.createOrder(String.valueOf(outTradeNo), totalAmount, "magicbazaar", "测试", response);

        if (tradePagePayResponse.isSuccess()) {
            log.info("===============alipay start===============");
            log.info("订单号：{}", outTradeNo);
            log.info("支付金额：{}", totalAmount);
        } else {
            log.info("===============alipay start===============");
            log.info("支付，验证失败");
        }
        log.info("===============alipay end===============");
    }

    @Override
    public void returnUrl(HttpServletRequest request) throws AlipayApiException {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();

        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }

            //乱码
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }

        // 验签
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);

        if (signVerified) {
            // 订单号
            String outTradeNo = params.get("out_trade_no");
            // 支付宝订单号
            String tradeNo = params.get("trade_no");
            // 支付金额
            String totalAmount = params.get("total_amount");

            // 更新支付状态
            orderMapper.update(new Order().setOrderTypeId(9L), new QueryWrapper<Order>().eq("order_number", outTradeNo));

            // 更新订单详情状态
            Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("order_number", outTradeNo));
            List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));

            for (OrderInfo orderInfo : orderInfoList) {
                orderInfoMapper.updateById(orderInfo.setOrderTypeId(2L));
            }

            // 记录信息
            log.info("===============alipay start===============");
            log.info("订单号：{}", outTradeNo);
            log.info("支付宝订单号：{}", tradeNo);
            log.info("支付金额：{}", totalAmount);
        } else {
            log.info("===============alipay start===============");
            log.info("支付，验证失败");
        }
        log.info("===============alipay end===============");
    }


    @Override
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException {

        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();

        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }

            //乱码
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }



        // 验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);

        if (signVerified) {
            //验证成功

            // 商户需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号
            // 判断 total_amount 是否确实为该订单的实际金额（即商户订单创建时的金额）
            // 校验通知中的 seller_id（seller_email) 是否为 out_trade_no 这笔单据的对应的操作方
            // 验证 app_id 是否为该商户本身

            // 商户订单号
            String out_trade_no = params.get("out_trade_no");
            String order = AlipayUtil.findOrder(out_trade_no);
            if (order == null) {
                return;
            }

            // 订单的实际金额
//            String totalAmount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//            if (!aliPayOrder.getTotalAmount().equals(new BigDecimal(totalAmount))) {
//                return;
//            }
//            //商品卖方id
//            String sellerIid = new String(request.getParameter("seller_id").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//            if (!aliPayOrder.getSellerId().equals(sellerIid)) {
//                return;
//            }
//            //appid
//            String app_id = new String(request.getParameter("app_id").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//            if (!AlipayConfig.APP_ID.equals(app_id)) {
//                return;
//            }
//
//            //buyer_id
//            String buyer_id = new String(request.getParameter("buyer_id").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//            aliPayOrder.setBuyerId(buyer_id);
//
//
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//            //交易创建时间
//            String gmt_create = new String(request.getParameter("gmt_create").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//            if (gmt_create != null) {
//                Date gmtCreate = simpleDateFormat.parse(gmt_create);
//                aliPayOrder.setGmtCreate(gmtCreate);
//            }
//
//            //交易付款时间
//            String gmt_payment = new String(request.getParameter("gmt_create").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//            if (gmt_payment != null) {
//                Date gmtPayment = simpleDateFormat.parse(gmt_payment);
//                aliPayOrder.setGmtPayment(gmtPayment);
//            }
//
//            //交易结束时间
//            String gmt_close = new String(request.getParameter("gmt_close").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//            if (gmt_close != null) {
//                Date gmtClose = simpleDateFormat.parse(gmt_close);
//                aliPayOrder.setGmtClose(gmtClose);
//            }
//
//            //交易状态
//            String trade_status = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//            aliPayOrder.setTradeStatus(trade_status);
//            aliPayService.updateAliPayOrder(aliPayOrder);
//            if (trade_status.equals("TRADE_SUCCESS")) { //默认开启
//                //调用各个系统的接口
//                String url = "";
//                if (ProjectConstants.PROJECT_SIGN_TN.equals(aliPayOrder.getProjectSign())) {//泰能热电电子招投标系统
//                    url = DictUtils.getDictValue("tn_project_interface_address", "supplier_ukey_renew");
//                }
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("supplierId", aliPayOrder.getSellerId());
//                jsonObject.put("expiredTime", aliPayOrder.getNowExpiredTime());
//                HttpUtils.sendPost(url, JSONObject.toJSONString(jsonObject));
//            }
//            response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
//            response.getWriter().write("success");
//            response.getWriter().flush();
//            response.getWriter().close();
//        } else {
//            response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
//            response.getWriter().write("fail");
//            response.getWriter().flush();
//            response.getWriter().close();
//        }
        }
    }
}
