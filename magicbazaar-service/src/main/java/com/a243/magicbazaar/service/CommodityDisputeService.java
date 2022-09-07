package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityDisputeParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeVo;
import com.alipay.api.AlipayApiException;

import java.util.List;

public interface CommodityDisputeService {
    /**
     * 查询
     *
     * @param commodityDisputeParam
     * @return
     */
    Result<List<CommodityDisputeVo>> list(CommodityDisputeParam commodityDisputeParam);

    /**
     * 显示
     *
     * @param commodityDisputeParam
     * @param <T>
     * @return
     */
    <T> Result<T> on(CommodityDisputeParam commodityDisputeParam);

    /**
     * 不显示
     *
     * @param commodityDisputeParam
     * @param <T>
     * @return
     */
    <T> Result<T> off(CommodityDisputeParam commodityDisputeParam);

    /**
     * 删除
     *
     * @param commodityDisputeParam
     * @param <T>
     * @return
     */
    <T> Result<T> delete(CommodityDisputeParam commodityDisputeParam);

    /**
     * 换货
     *
     * @param commodityDisputeParam
     * @param <T>
     * @return
     */
    <T> Result<T> change(CommodityDisputeParam commodityDisputeParam);

    /**
     * 退货
     *
     * @param commodityDisputeParam
     * @param <T>
     * @return
     */
    <T> Result<T> exit(CommodityDisputeParam commodityDisputeParam) throws AlipayApiException;

    /**
     * 换货一个
     * @param commodityDisputeParam
     * @param <T>
     * @return
     */
    <T> Result<T> changeOne(CommodityDisputeParam commodityDisputeParam);

    /**
     * 退货一个
     * @param commodityDisputeParam
     * @param <T>
     * @return
     */
    <T> Result<T> exitOne(CommodityDisputeParam commodityDisputeParam) throws AlipayApiException;

    Result<String> disputeCounts();
}
