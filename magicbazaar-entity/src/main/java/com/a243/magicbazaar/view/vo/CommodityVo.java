package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.Commodity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CommodityVo extends Commodity {
    @Serial
    private static final long serialVersionUID = 2726762397861857635L;
    private Long id;
    private String title;
    private String subTitle;
    private BigDecimal price;
    private Long commodityNumber;
    private String description;
    private String photo;
    private String fullPhoto;
    private String commodityBrandName;
    private String commodityTypeName;
    private Integer commodityStatus;
    private LocalDate previewEndTime;
    private String previewInfo;
    private LocalDate saleStartTime;
    private BigDecimal promotionPrice;
    private Integer promotionType;
    private LocalDateTime promotionStartTime;
    private LocalDateTime promotionEndTime;
    private Integer publishStatus;
    private Integer deleteStatus;
    private Integer commodityVerify;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
