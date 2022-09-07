package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderParam {
    private PageParam pageParam;
    private Integer page;
    private String stars;
    private String type;
    private String commentInfo;
    private Integer pageSize;
    private String commodityDisputeInfoId;
    private String id;
    private String orderNumber;
    private String nickname;
    private String paypalId;
    private String orderTypeId;
    private String publishStatus;
    private String deleteStatus;
    private String orderId;
    private String orderInfoId;
}
