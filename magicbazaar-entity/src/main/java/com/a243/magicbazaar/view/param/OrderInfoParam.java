package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderInfoParam {
    private PageParam pageParam;
    private String id;
    private String orderId;
}
