package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.CommodityDispute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CommodityDisputeVo extends CommodityDispute {
    @Serial
    private static final long serialVersionUID = 2783856166114345832L;
    private Long id;
    private String disputeNumber;
    private Long orderId;
    private String orderNumber;
    private Integer disputeStatus;
    private Integer publishStatus;
    private Integer deleteStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
