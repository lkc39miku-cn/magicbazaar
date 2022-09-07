package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommodityTypeParam {
    private Boolean tree = false;
    private String id;
    private String commodityTypeParentId;
    private String commodityTypeId;
    private String name;
    private String commodityTypeName;
    private String publishStatus;
}
