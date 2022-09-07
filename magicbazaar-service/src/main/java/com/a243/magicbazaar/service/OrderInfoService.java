package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.OrderInfoParam;
import com.a243.magicbazaar.view.vo.OrderInfoVo;

import java.util.List;

public interface OrderInfoService {
    /**
     * 查询
     *
     * @param orderInfoParam
     * @return
     */
    Result<List<OrderInfoVo>> list(OrderInfoParam orderInfoParam);

    /**
     * 显示
     *
     * @param orderInfoParam
     * @param <T>
     * @return
     */
    <T> Result<T> on(OrderInfoParam orderInfoParam);

    /**
     * 不显示
     *
     * @param orderInfoParam
     * @param <T>
     * @return
     */
    <T> Result<T> off(OrderInfoParam orderInfoParam);

    /**
     * 删除
     *
     * @param orderInfoParam
     * @param <T>
     * @return
     */
    <T> Result<T> delete(OrderInfoParam orderInfoParam);

    /**
     * 发货
     *
     * @param orderInfoParam
     * @param <T>
     * @return
     */
    <T> Result<T> fire(OrderInfoParam orderInfoParam);
}
