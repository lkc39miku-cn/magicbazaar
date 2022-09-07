package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.StoreHouseIn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreHouseInVo extends StoreHouseIn {
    @Serial
    private static final long serialVersionUID = -423209584625580615L;
    private Long id;
    private String commodityName;
    private Long innNumber;
    private Long realInNumber;
    private String storeHouseInInfo;
    private Integer storeHouseInStatus;
    private Integer deleteStatus;
    private String staffName;
    private String targetName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
