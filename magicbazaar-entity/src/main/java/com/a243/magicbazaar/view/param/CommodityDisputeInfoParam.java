package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommodityDisputeInfoParam {
    private PageParam pageParam;
    private String commodityDisputeId;
}
