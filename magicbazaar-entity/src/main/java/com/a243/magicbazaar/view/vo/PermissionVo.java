package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PermissionVo extends Permission {
    @Serial
    private static final long serialVersionUID = 7267184253849627642L;
    private Long id;
    private String name;
    private String path;
    private String description;
    private Integer deleteStatus;
    private Long menuId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
