package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.OrderTypeParam;
import com.a243.magicbazaar.view.vo.OrderTypeVo;

import java.util.List;

public interface OrderTypeService {
    /**
     * 查询
     *
     * @param orderTypeParam
     * @return
     */
    Result<List<OrderTypeVo>> list(OrderTypeParam orderTypeParam);

    /**
     * 显示
     *
     * @param orderTypeParam
     * @param <T>
     * @return
     */
    <T> Result<T> on(OrderTypeParam orderTypeParam);

    /**
     * 不显示
     *
     * @param orderTypeParam
     * @param <T>
     * @return
     */
    <T> Result<T> off(OrderTypeParam orderTypeParam);
}
