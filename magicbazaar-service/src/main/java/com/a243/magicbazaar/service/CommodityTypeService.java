package com.a243.magicbazaar.service;


import com.a243.magicbazaar.dao.entity.CommodityType;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityTypeParam;
import com.a243.magicbazaar.view.vo.CommodityTypeVo;

import java.util.List;

public interface CommodityTypeService {
    /**
     * 商品类型列表
     *
     * @param commodityTypeParam 类型参数
     * @return 结果集
     */
    Result<List<CommodityTypeVo>> list(CommodityTypeParam commodityTypeParam);

    /**
     * 删除商品类型
     *
     * @param commodityTypeParam 商品类型参数
     * @param <T>                泛型
     * @return 结果集
     */
    <T> Result<T> delete(CommodityTypeParam commodityTypeParam);

    /**
     * 启用商品类型
     *
     * @param commodityTypeParam 商品类型参数
     * @param <T>                泛型
     * @return 结果集
     */
    <T> Result<T> on(CommodityTypeParam commodityTypeParam);

    /**
     * 禁用商品类型
     *
     * @param commodityTypeParam 商品类型参数
     * @param <T>                泛型
     * @return 结果集
     */
    <T> Result<T> off(CommodityTypeParam commodityTypeParam);

    /**
     * 添加商品类型
     *
     * @param commodityType 商品类型参数
     * @param <T>           泛型
     * @return 结果集
     */
    <T> Result<T> add(CommodityType commodityType);

    /**
     * 修改商品类型
     *
     * @param commodityType 商品类型参数
     * @param <T>           泛型
     * @return 结果集
     */
    <T> Result<T> update(CommodityType commodityType);

    /**
     * 查询一个
     *
     * @param commodityTypeParam
     * @return
     */
    Result<CommodityTypeVo> one(CommodityTypeParam commodityTypeParam);

    /**
     * 子级
     *
     * @param commodityTypeParam
     * @return
     */
    Result<List<CommodityTypeVo>> child(CommodityTypeParam commodityTypeParam);
}
