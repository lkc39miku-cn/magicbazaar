package com.a243.magicbazaar.dao.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class MenuPermission implements Serializable {
    @Serial
    private static final long serialVersionUID = 8556155701678716710L;
    private Long id;
    private Long falseId;
    private String name;
    private Integer publishStatus;
    private Integer deleteStatus;
    private String menuValue;
    private String permissionValue;
    private Long parentId;
    private Integer isPermission;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
