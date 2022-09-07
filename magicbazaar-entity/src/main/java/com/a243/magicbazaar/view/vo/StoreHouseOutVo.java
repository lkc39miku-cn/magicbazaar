package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.StoreHouseOut;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreHouseOutVo extends StoreHouseOut {
    @Serial
    private static final long serialVersionUID = -2338304876247177622L;
    private Long id;
    private String commodityName;
    private Long outNumber;
    private Long realOutNumber;
    private String storeHouseOutInfo;
    private Integer storeHouseOutStatus;
    private Integer deleteStatus;
    private String staffName;
    private String targetName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
