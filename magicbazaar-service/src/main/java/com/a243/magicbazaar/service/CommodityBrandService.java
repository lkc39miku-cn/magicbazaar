package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.CommodityBrand;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityBrandParam;
import com.a243.magicbazaar.view.vo.CommodityBrandVo;

import java.util.List;

public interface CommodityBrandService {
    /**
     * 获取商品品牌列表
     *
     * @param commodityBrandParam 参数
     * @return 结果集
     */
    Result<List<CommodityBrandVo>> list(CommodityBrandParam commodityBrandParam);

    /**
     * 添加商品品牌
     *
     * @param commodityBrand 商品品牌参数
     * @param <T>            泛型
     * @return 结果集
     */
    <T> Result<T> add(CommodityBrand commodityBrand);

    /**
     * 更新商品品牌
     *
     * @param commodityBrand 商品品牌参数
     * @param <T>            泛型
     * @return 结果集
     */
    <T> Result<T> update(CommodityBrand commodityBrand);

    /**
     * 删除品牌
     *
     * @param commodityBrandParam 品牌参数
     * @param <T>                 泛型
     * @return 结果集
     */
    <T> Result<T> delete(CommodityBrandParam commodityBrandParam);

    /**
     * 启用品牌
     *
     * @param commodityBrandParam 品牌参数
     * @param <T>                 泛型
     * @return 结果集
     */
    <T> Result<T> on(CommodityBrandParam commodityBrandParam);

    /**
     * 禁用品牌
     *
     * @param commodityBrandParam 品牌参数
     * @param <T>                 泛型
     * @return 结果集
     */
    <T> Result<T> off(CommodityBrandParam commodityBrandParam);
}
