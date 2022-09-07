package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.StaffRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StaffRoleVo extends StaffRole {
    @Serial
    private static final long serialVersionUID = -5042344317970798313L;
    private Long id;
    private Long staffId;
    private String staffName;
    private Long roleId;
    private String roleName;
    private Integer deleteStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
