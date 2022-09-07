package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommodityParam {
    private PageParam pageParam;
    private String id;
    private String num;
    private Integer page;
    private Integer limit;
    private String commodityTypeParentId;
    private String commodityTypeId;
    private String commodityBrandId;
    private String minPrice;
    private String maxPrice;
    private String orderByDesc;
    private String title;
    private String commodityStatus;
    private String promotionType;
    private String deleteStatus;
    private String publishStatus;
    private String verifyStatus;
    private String value;
    private Boolean check;
}
