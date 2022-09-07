package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.CommodityType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CommodityTypeVo extends CommodityType {
    @Serial
    private static final long serialVersionUID = -7337225853252441167L;
    private Long id;
    private String name;
    private Integer publishStatus;
    private Long sort;
    private String menuIcon;
    private Long parentId;
    private Integer isMenu;
    private List<CommodityTypeVo> children;
    private String number;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
