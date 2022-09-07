package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.Paypal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PaypalVo extends Paypal {
    @Serial
    private static final long serialVersionUID = -1554973246495507920L;
    private Long id;
    private String name;
    private String icon;
    private Integer publishStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
