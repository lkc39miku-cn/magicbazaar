package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RoleVo extends Role {
    @Serial
    private static final long serialVersionUID = -8204199285388942800L;
    private Long id;
    private String name;
    private String description;
    private Integer publishStatus;
    private Integer deleteStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
