package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MenuParam {
    private PageParam pageParam;
    private String id;
    private String parentId;
    private String falseId;
    private String check;
    private String parentMenu;
    private String childMenu;
    private String orderMenu;
    private String name;
    private String path;
    private String description;
    private String isPermission;
}
