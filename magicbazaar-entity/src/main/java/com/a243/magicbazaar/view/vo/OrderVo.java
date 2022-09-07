package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.Order;
import com.a243.magicbazaar.dao.entity.OrderInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OrderVo extends Order {
    @Serial
    private static final long serialVersionUID = -1033527098282207126L;
    private Long id;
    private String orderNumber;
    private String userName;
    private String paypalName;
    private BigDecimal money;
    private Long orderTypeId;
    private String orderTypeName;
    private List<OrderInfoVo> child;
    private Boolean check;
    private Integer publishStatus;
    private Integer deleteStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
