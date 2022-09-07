package com.a243.magicbazaar.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单
 */
@Data
@Accessors(chain = true)
@TableName(value = "mb_menu")
public class Menu implements Serializable {
    @Serial
    private static final long serialVersionUID = 7177195838380466986L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "parent_id")
    private Long pid;

    @TableField(value = "name")
    private String title;

    @TableField(value = "icon")
    private String icon;

    @TableField(value = "path")
    private String href;

    @TableField(value = "target")
    private String target;

    @TableField(value = "sort")
    private Long sort;

    @TableField(value = "publish_status")
    private Integer status;

    @TableField(value = "description")
    private String description;

    @TableField(value = "delete_status")
    private Integer deleteStatus;

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
