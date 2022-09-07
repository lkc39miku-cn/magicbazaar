package com.a243.magicbazaar.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Purchase implements Serializable {
    @Serial
    private static final long serialVersionUID = 1425654034871237995L;
    private Long id;
    private Long commodityId;
    private Long purchaseNumber;
    private BigDecimal cost;
    private String purchaseInfo;
    private Long realPurchaseNumber;
    private BigDecimal realCost;
    private String reply;
    private Integer purchaseStatus;
    private Long staffId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    private LocalDateTime updateTime;
}
