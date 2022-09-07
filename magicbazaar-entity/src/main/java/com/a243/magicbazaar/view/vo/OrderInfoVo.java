package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.OrderInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OrderInfoVo extends OrderInfo {
    private Long id;
    private Long orderId;
    private String commodityName;
    private String commodityPhoto;
    private BigDecimal price;
    private Long number;
    private BigDecimal allPrice;
    private String userAddressName;
    private String userName;
    private String userPhone;
    private String orderTypeName;
    private Integer publishStatus;
    private Integer deleteStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
