package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityParam;
import com.a243.magicbazaar.view.vo.CommodityVerifyVo;
import com.a243.magicbazaar.view.vo.CommodityVo;
import com.a243.magicbazaar.view.vo.StoreHouseVo;

import java.time.LocalDate;
import java.util.List;

public interface CommodityService {
    /**
     * 商品数据
     *
     * @param commodityParam 商品参数
     * @return 结果集
     */
    Result<List<CommodityVo>> list(CommodityParam commodityParam);

    /**
     * 添加商品
     *
     * @param commodity 商品参数
     * @param <T>       泛型
     * @return 结果集
     */
    <T> Result<T> add(Commodity commodity);

    /**
     * 修改商品
     *
     * @param commodity 审批参数
     * @param <T>       泛型
     * @return 结果集
     */
    <T> Result<T> update(Commodity commodity);

    /**
     * 删除商品
     *
     * @param commodityParam
     * @param <T>
     * @return
     */
    <T> Result<T> delete(CommodityParam commodityParam);

    /**
     * 进货
     *
     * @param commodityParam
     * @param <T>
     * @return
     */
    <T> Result<T> addNum(CommodityParam commodityParam);

    /**
     * 下架商品
     *
     * @param commodityParam
     * @param <T>
     * @return
     */
    <T> Result<T> off(CommodityParam commodityParam);

    /**
     * 上架
     *
     * @param commodityParam
     * @param <T>
     * @return
     */
    <T> Result<T> on(CommodityParam commodityParam);

    /**
     * 剩余数量
     *
     * @param commodityParam
     * @return
     */
    Result<StoreHouseVo> storeNum(CommodityParam commodityParam);

    /**
     * 未过审原因
     *
     * @param commodityParam
     * @return
     */
    Result<CommodityVerifyVo> failInfo(CommodityParam commodityParam);

    /**
     * 再次审核
     *
     * @param commodityParam
     * @param <T>
     * @return
     */
    <T> Result<T> again(CommodityParam commodityParam);

    /**
     * 检查重名
     * @param commodityParam
     * @param <T>
     * @return
     */
    <T> Result<T> checkName(CommodityParam commodityParam);

    /**
     * 销售时间
     * @param commodityParam
     * @param <T>
     * @return
     */
    <T> Result<T> checkTime(CommodityParam commodityParam);

    Result<String> getMoney(LocalDate startTime, LocalDate endTime);

    Result<String> outMoney(LocalDate startTime, LocalDate endTime);
}
