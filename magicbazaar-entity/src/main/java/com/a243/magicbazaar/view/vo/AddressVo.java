package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.Address;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AddressVo extends Address {
    @Serial
    private static final long serialVersionUID = 7524537422576463347L;
    private Long id;
    private String name;
    private Long parentId;
    private Long level;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
