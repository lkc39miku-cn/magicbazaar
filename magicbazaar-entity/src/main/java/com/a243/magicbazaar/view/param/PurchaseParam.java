package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PurchaseParam {
    private PageParam pageParam;
    private String id;
    private String commodityName;
    private String purchaseStatus;
    private String verifyStatus;
    private String financeStatus;
    private String staffName;
}
