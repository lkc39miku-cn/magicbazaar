package com.a243.magicbazaar.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CommodityDisputeComment implements Serializable {
    @Serial
    private static final long serialVersionUID = -3928775218234854548L;
    private Long id;
    private Long commodityDisputeInfoId;
    private Long staffId;
    private Long userId;
    private String context;
    private String contextImg;
    private String replyContext;
    private String replyContextImg;

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
