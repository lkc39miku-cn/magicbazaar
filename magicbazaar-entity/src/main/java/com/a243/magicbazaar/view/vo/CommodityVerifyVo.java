package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.CommodityVerify;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CommodityVerifyVo extends CommodityVerify {
    @Serial
    private static final long serialVersionUID = 7930116803832268897L;
    private Long id;
    private String commodityName;
    private Integer verifyStatus;
    private String verifyInfo;
    private String staffName;
    private String targetName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
