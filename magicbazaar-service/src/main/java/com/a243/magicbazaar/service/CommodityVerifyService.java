package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityVerifyParam;
import com.a243.magicbazaar.view.vo.CommodityVerifyVo;

import java.util.List;

public interface CommodityVerifyService {
    /**
     * 查询审核商品
     *
     * @param commodityVerifyParam 审核参数
     * @return 结果集
     */
    Result<List<CommodityVerifyVo>> list(CommodityVerifyParam commodityVerifyParam);

    /**
     * 通过商品审核
     *
     * @param commodityVerifyParam 审核参数
     * @param <T>                  泛型
     * @return 结果集
     */
    <T> Result<T> success(CommodityVerifyParam commodityVerifyParam);

    /**
     * 拒绝商品审核
     *
     * @param commodityVerifyParam 审核参数
     * @param <T>                  泛型
     * @return 结果集
     */
    <T> Result<T> fail(CommodityVerifyParam commodityVerifyParam);

    /**
     * 删除商品审核
     *
     * @param commodityVerifyParam 审核参数
     * @param <T>                  泛型
     * @return 结果集
     */
    <T> Result<T> delete(CommodityVerifyParam commodityVerifyParam);
}
