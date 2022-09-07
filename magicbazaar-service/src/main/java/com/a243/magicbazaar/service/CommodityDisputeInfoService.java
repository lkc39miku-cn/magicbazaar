package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityDisputeInfoParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeInfoVo;

import java.util.List;

public interface CommodityDisputeInfoService {
    /**
     * 查询
     * @param commodityDisputeInfoParam
     * @return
     */
    Result<List<CommodityDisputeInfoVo>> list(CommodityDisputeInfoParam commodityDisputeInfoParam);
}
