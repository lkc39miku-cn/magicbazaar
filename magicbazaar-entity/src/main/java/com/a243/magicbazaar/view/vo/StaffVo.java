package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.Staff;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class StaffVo extends Staff {
    @Serial
    private static final long serialVersionUID = 6297958754494940691L;
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Integer publishStatus;
    private Integer deleteStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
