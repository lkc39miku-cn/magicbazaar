package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.OrderType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OrderTypeVo extends OrderType {
    @Serial
    private static final long serialVersionUID = 3282127877947942359L;
    private Long id;
    private String name;
    private Integer publishStatus;
    private String number;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
