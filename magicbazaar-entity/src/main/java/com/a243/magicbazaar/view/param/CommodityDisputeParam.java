package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommodityDisputeParam {
    private PageParam pageParam;
    private String id;
    private String disputeNumber;
    private String disputeStatus;
    private String orderNumber;
    private String publishStatus;
    private String deleteStatus;
}
