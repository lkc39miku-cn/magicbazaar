package com.a243.magicbazaar.service.reception;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.OrderParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeCommentVo;
import com.a243.magicbazaar.view.vo.OrderVo;

import java.util.List;

public interface ReOrderService {
    /**
     * 查询
     * @param orderParam
     * @return
     */
    Result<List<OrderVo>> list(OrderParam orderParam);

    /**
     * 换货全部
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> changeAll(OrderParam orderParam);

    /**
     * 信息
     * @param orderParam
     * @return
     */
    Result<List<CommodityDisputeCommentVo>> disputeInfo(OrderParam orderParam);

    /**
     * 全部退货
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> exitAll(OrderParam orderParam);

    /**
     * 退一件
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> exitOne(OrderParam orderParam);

    /**
     * 收货一个
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> haveOne(OrderParam orderParam);

    /**
     * 收货全部
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> haveAll(OrderParam orderParam);

    /**
     * 评分
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> rate(OrderParam orderParam);

    /**
     * 换一件
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> changeOne(OrderParam orderParam);

    /**
     * 用户订单数
     * @return
     */
    Result<String> allOrder();

    /**
     * 用户总花费
     * @return
     */
    Result<String> allPrice();

    /**
     * 用户注册时间
     * @return
     */
    Result<String> allDate();

    /**
     * 取消订单
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> closePaypal(OrderParam orderParam);

    /**
     * 删除订单
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> deleteOne(OrderParam orderParam);

    /**
     * 删除所有订单
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> deleteAll(OrderParam orderParam);

    <T> Result<T> goSuccess(OrderParam orderParam);
}
