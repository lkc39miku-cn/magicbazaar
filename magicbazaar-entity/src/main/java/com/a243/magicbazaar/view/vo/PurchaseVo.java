package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.Purchase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PurchaseVo extends Purchase {
    @Serial
    private static final long serialVersionUID = -9160037774121962506L;
    private Long id;
    private String commodityName;
    private Long purchaseNumber;
    private BigDecimal cost;
    private String purchaseInfo;
    private Long realPurchaseNumber;
    private BigDecimal realCost;
    private String reply;
    private Integer purchaseStatus;
    private String purchaseName;
    private String purchaseOk;
    private String financeName;
    private String financeOk;
    private String staffName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
