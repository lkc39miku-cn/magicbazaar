package com.a243.magicbazaar.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -4833524894259543014L;
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String avatar;
    private String background;
    private String phone;
    private String email;
    private Integer publishStatus;
    private Integer deleteStatus;
    private Long staffId;

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
