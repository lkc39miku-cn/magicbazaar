package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.Menu;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MenuVo extends Menu {
    @Serial
    private static final long serialVersionUID = -8193543904870460076L;
    private Long id;
    private Long pid;
    private String title;
    private String icon;
    private String href;
    private String target;
    private List<MenuVo> child;
}
