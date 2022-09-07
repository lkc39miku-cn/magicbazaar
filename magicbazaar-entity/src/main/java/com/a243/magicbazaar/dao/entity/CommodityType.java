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
 * 商品类型
 */
@Data
@Accessors(chain = true)
@TableName(value = "mb_commodity_type")
public class CommodityType implements Serializable {
    @Serial
    private static final long serialVersionUID = 9133001222294092494L;
    /**
     * 商品类型主键
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 显示状态 1 显示 0 不显示
     */
    private Integer publishStatus;
    /**
     * 排序
     */
    private Long sort;
    /**
     * 菜单icon路径
     */
    @TableField(value = "icon")
    private String menuIcon;
    /**
     * 父级菜单id
     */
    private Long parentId;
    /**
     * 是否为菜单 1 是菜单 0 不是菜单
     */
    @TableField(value = "is_type")
    private Integer isMenu;

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
