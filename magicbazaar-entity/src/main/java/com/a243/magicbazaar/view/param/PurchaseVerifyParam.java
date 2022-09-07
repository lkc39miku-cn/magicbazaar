package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PurchaseVerifyParam {
    private PageParam pageParam;
    private String id;
    private String commodityName;
    private String infoName;
    private String verifyStatus;
    private String StaffName;
    private String targetName;
}
