package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.CommodityBrand;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CommodityBrandVo extends CommodityBrand {
    @Serial
    private static final long serialVersionUID = -6341236873281330746L;
    private Long id;
    private String name;
    private String firstLetter;
    private Integer publishStatus;
    private String logo;
    private String bigLogo;
    private String number;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
