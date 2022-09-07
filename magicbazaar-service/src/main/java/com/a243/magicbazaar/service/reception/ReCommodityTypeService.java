package com.a243.magicbazaar.service.reception;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityTypeParam;
import com.a243.magicbazaar.view.vo.CommodityTypeVo;

import java.util.List;

public interface ReCommodityTypeService {
    /**
     * 查询
     * @param commodityTypeParam
     * @return
     */
    Result<List<CommodityTypeVo>> list(CommodityTypeParam commodityTypeParam);
}
