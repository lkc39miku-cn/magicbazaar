package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.FinanceVerifyParam;
import com.a243.magicbazaar.view.vo.FinanceVerifyVo;

import java.util.List;

public interface FinanceVerifyService {
    /**
     * 查询财务审核
     *
     * @param financeVerifyParam 财务参数
     * @return 结果集
     */
    Result<List<FinanceVerifyVo>> list(FinanceVerifyParam financeVerifyParam);

    /**
     * 审核通过
     *
     * @param financeVerifyParam 财务参数
     * @param <T>                泛型
     * @return 结果集
     */
    <T> Result<T> success(FinanceVerifyParam financeVerifyParam);

    /**
     * 删除记录
     *
     * @param financeVerifyParam 财务参数
     * @param <T>                泛型
     * @return 结果集
     */
    <T> Result<T> delete(FinanceVerifyParam financeVerifyParam);

    /**
     * 不通过
     *
     * @param financeVerifyParam 财务参数
     * @param <T>                泛型
     * @return 结果集
     */
    <T> Result<T> fail(FinanceVerifyParam financeVerifyParam);
}
