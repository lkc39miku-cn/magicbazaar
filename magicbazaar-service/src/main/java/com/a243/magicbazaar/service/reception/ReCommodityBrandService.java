package com.a243.magicbazaar.service.reception;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityBrandParam;
import com.a243.magicbazaar.view.vo.CommodityBrandVo;

import java.util.List;

public interface ReCommodityBrandService {
    /**
     * 查询
     * @param commodityBrandParam
     * @return
     */
    Result<List<CommodityBrandVo>> list(CommodityBrandParam commodityBrandParam);
}
