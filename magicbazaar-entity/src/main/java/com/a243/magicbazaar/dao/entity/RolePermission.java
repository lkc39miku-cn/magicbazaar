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
public class RolePermission implements Serializable {
    @Serial
    private static final long serialVersionUID = -5408135467685568418L;
    private Long id;
    private Long roleId;
    private Long permissionId;
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
