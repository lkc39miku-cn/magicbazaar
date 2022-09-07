package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StoreHouseInParam {
    private PageParam pageParam;
    private String id;
    private String commodityName;
    private String staffName;
    private String targetName;
    private String storeHouseInStatus;
    private String deleteStatus;
}
