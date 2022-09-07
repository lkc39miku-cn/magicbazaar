package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.Purchase;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.PurchaseParam;
import com.a243.magicbazaar.view.vo.PurchaseVo;

import java.util.List;

public interface PurchaseService {
    /**
     * 查询订单信息
     *
     * @param purchaseParam 订单参数
     * @return 结果集
     */
    Result<List<PurchaseVo>> list(PurchaseParam purchaseParam);

    /**
     * 添加
     *
     * @param purchase
     * @param <T>
     * @return
     */
    <T> Result<T> add(Purchase purchase);

    /**
     * 修改
     *
     * @param purchase
     * @param <T>
     * @return
     */
    <T> Result<T> update(Purchase purchase);

    /**
     * 再次审核
     *
     * @param purchaseParam
     * @param <T>
     * @return
     */
    <T> Result<T> again(PurchaseParam purchaseParam);

    /**
     * 回采
     *
     * @param purchase
     * @param <T>
     * @return
     */
    <T> Result<T> reUpdate(Purchase purchase);

    /**
     * 拒绝原因
     *
     * @param purchaseParam
     * @param <T>
     * @return
     */
    Result<String> info(PurchaseParam purchaseParam);

    /**
     * 完成订单
     *
     * @param purchaseParam
     * @param <T>
     * @return
     */
    <T> Result<T> yes(PurchaseParam purchaseParam);

    /**
     * 放弃订单
     *
     * @param purchaseParam
     * @param <T>
     * @return
     */
    <T> Result<T> no(PurchaseParam purchaseParam);

    /**
     * 删除订单
     *
     * @param purchaseParam
     * @param <T>
     * @return
     */
    <T> Result<T> delete(PurchaseParam purchaseParam);
}
