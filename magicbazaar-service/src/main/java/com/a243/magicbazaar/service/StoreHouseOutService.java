package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.StoreHouseOut;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.StoreHouseOutParam;
import com.a243.magicbazaar.view.vo.StoreHouseOutVo;

import java.util.List;

public interface StoreHouseOutService {
    /**
     * 查询出库记录
     *
     * @param storeHouseOutParam 出库参数
     * @return 结果集
     */
    Result<List<StoreHouseOutVo>> list(StoreHouseOutParam storeHouseOutParam);

    /**
     * 出库
     *
     * @param storeHouseOut
     * @param <T>
     * @return
     */
    <T> Result<T> out(StoreHouseOut storeHouseOut);

    /**
     * 删除
     *
     * @param storeHouseOutParam
     * @param <T>
     * @return
     */
    <T> Result<T> delete(StoreHouseOutParam storeHouseOutParam);
}
