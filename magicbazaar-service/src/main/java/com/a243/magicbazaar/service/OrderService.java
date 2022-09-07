package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.OrderParam;
import com.a243.magicbazaar.view.vo.OrderVo;

import java.util.List;

public interface OrderService {
    /**
     * 查询
     *
     * @param orderParam
     * @return
     */
    Result<List<OrderVo>> list(OrderParam orderParam);

    /**
     * 显示
     *
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> on(OrderParam orderParam);

    /**
     * 不显示
     *
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> off(OrderParam orderParam);

    /**
     * 删除
     *
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> delete(OrderParam orderParam);

    /**
     * 发货
     *
     * @param orderParam
     * @param <T>
     * @return
     */
    <T> Result<T> fire(OrderParam orderParam);

    /**
     *
     * @return
     */
    Result<String> orderCounts();

    Result<String> moneyCounts();
}
