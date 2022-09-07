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
public class StoreHouseIn implements Serializable {
    @Serial
    private static final long serialVersionUID = 2408506839595633972L;
    private Long id;
    private Long purchaseId;
    private Long inNumber;
    private Long realInNumber;
    private String storeHouseInInfo;
    private Integer storeHouseInStatus;
    private Integer deleteStatus;
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
