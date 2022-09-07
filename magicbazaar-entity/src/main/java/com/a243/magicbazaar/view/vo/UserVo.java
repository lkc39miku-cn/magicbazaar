package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UserVo extends User {
    @Serial
    private static final long serialVersionUID = -4685858880017733908L;
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String gender;
    private LocalDate birthday;
    private String avatar;
    private String background;
    private String phone;
    private String email;
    private Integer publishStatus;
    private Integer deleteStatus;
    private Integer commentStatus;
    private String staffName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
