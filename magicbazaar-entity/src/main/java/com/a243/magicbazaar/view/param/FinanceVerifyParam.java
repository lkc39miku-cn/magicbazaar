package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FinanceVerifyParam {
    private PageParam pageParam;
    private String id;
    private String commodityName;
    private String verifyStatus;
    private String StaffName;
    private String infoName;
    private String targetName;
}
