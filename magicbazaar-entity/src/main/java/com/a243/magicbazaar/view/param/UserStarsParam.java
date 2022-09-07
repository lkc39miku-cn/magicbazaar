package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserStarsParam {
    private PageParam pageParam;
    private String id;
    private String username;
    private String commodityName;
}
