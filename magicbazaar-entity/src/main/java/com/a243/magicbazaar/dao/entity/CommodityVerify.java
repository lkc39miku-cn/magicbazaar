package com.a243.magicbazaar.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品审核
 */
@Data
@Accessors(chain = true)
public class CommodityVerify implements Serializable {
    @Serial
    private static final long serialVersionUID = -5620987469278630361L;
    private Long id;
    private Long commodityId;
    private Integer verifyStatus;
    private String verifyInfo;
    private Long staffId;
    private Long targetId;

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
