package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.CommodityDisputeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CommodityDisputeInfoVo extends CommodityDisputeInfo {
    @Serial
    private static final long serialVersionUID = -7067787466436771102L;
    private String commodityDisputeNumber;
    private String orderInfoNumber;
    private String orderTypeName;
    private Long orderTypeId;
}
