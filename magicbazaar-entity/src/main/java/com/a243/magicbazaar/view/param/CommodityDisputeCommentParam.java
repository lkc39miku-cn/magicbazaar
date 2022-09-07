package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommodityDisputeCommentParam {
    private PageParam pageParam;
    private String id;
    private String commodityDisputeInfoId;
    private String replyContext;
}
