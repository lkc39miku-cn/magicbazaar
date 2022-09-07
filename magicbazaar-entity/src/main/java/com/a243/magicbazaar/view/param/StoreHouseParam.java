package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StoreHouseParam {
    private PageParam pageParam;
    private String id;
    private String commodityName;
    private String deleteStatus;
    private String num;
}
