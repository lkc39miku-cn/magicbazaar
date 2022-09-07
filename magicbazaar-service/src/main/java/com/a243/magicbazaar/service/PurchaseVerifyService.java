package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.PurchaseVerifyParam;
import com.a243.magicbazaar.view.vo.PurchaseVerifyVo;

import java.util.List;

public interface PurchaseVerifyService {
    /**
     * 采购审批信息
     *
     * @param purchaseVerifyParam 审批参数
     * @return 结果集
     */
    Result<List<PurchaseVerifyVo>> list(PurchaseVerifyParam purchaseVerifyParam);

    /**
     * 成功审批
     *
     * @param purchaseVerifyParam 审批参数
     * @param <T>                 泛型
     * @return 结果集
     */
    <T> Result<T> success(PurchaseVerifyParam purchaseVerifyParam);

    /**
     * 失败审批
     *
     * @param purchaseVerifyParam 审批参数
     * @param <T>                 泛型
     * @return 结果集
     */
    <T> Result<T> fail(PurchaseVerifyParam purchaseVerifyParam);

    /**
     * 删除审核记录
     *
     * @param purchaseVerifyParam 审批参数
     * @param <T>                 泛型
     * @return 结果集
     */
    <T> Result<T> delete(PurchaseVerifyParam purchaseVerifyParam);
}
