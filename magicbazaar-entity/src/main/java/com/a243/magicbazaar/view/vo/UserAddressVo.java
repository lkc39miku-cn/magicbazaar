package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.UserAddress;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UserAddressVo extends UserAddress {

    @Serial
    private static final long serialVersionUID = 4288533119065706488L;
    private Long id;
    private String userName;
    private String phone;
    private String addressName;
    private Integer addressStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
