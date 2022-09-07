package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.RolePermission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RolePermissionVo extends RolePermission {
    @Serial
    private static final long serialVersionUID = -2633340572399537433L;
    private Long id;
    private String roleName;
    private String permissionName;
    private Integer deleteStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
