package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.StoreHouse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreHouseVo extends StoreHouse {
    @Serial
    private static final long serialVersionUID = 7005007138239574975L;
    private Long id;
    private String commodityName;
    private Long stock;
    private Long lowStock;
    private Integer deleteStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
