package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.StoreHouse;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.StoreHouseParam;
import com.a243.magicbazaar.view.vo.StoreHouseVo;

import java.util.List;

public interface StoreHouseService {
    /**
     * 查询仓库信息
     *
     * @param storeHouseParam 信息参数
     * @return 结果集
     */
    Result<List<StoreHouseVo>> list(StoreHouseParam storeHouseParam);

    /**
     * 出货
     *
     * @param storeHouseParam
     * @param <T>
     * @return
     */
    <T> Result<T> out(StoreHouseParam storeHouseParam);

    /**
     * 修改
     *
     * @param storeHouse
     * @param <T>
     * @return
     */
    <T> Result<T> update(StoreHouse storeHouse);

    /**
     * 删除
     *
     * @param storeHouseParam
     * @param <T>
     * @return
     */
    <T> Result<T> delete(StoreHouseParam storeHouseParam);
}
