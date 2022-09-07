package com.a243.magicbazaar.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址
 */
@Data
@Accessors(chain = true)
@TableName(value = "mb_address")
public class Address implements Serializable {
    /**
     * 序列化
     */
    @Serial
    private static final long serialVersionUID = -5957056627856724126L;

    private Long id;
    private String name;
    private Long parentId;
    private Long level;

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
