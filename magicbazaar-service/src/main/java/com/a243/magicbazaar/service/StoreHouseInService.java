package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.StoreHouseIn;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.StoreHouseInParam;
import com.a243.magicbazaar.view.vo.StoreHouseInVo;

import java.util.List;

public interface StoreHouseInService {
    /**
     * 入库信息查询
     *
     * @param storeHouseInParam 入库参数
     * @return 结果集
     */
    Result<List<StoreHouseInVo>> list(StoreHouseInParam storeHouseInParam);

    /**
     * 修改信息
     *
     * @param storeHouseIn 入库信息参数
     * @param <T>          泛型
     * @return 结果集
     */
    <T> Result<T> update(StoreHouseIn storeHouseIn);

    /**
     * 山出入库记录
     *
     * @param storeHouseInParam 信息参数
     * @param <T>               参数
     * @return 结果集
     */
    <T> Result<T> delete(StoreHouseInParam storeHouseInParam);
}
