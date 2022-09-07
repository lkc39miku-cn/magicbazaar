package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.FinanceVerify;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FinanceVerifyVo extends FinanceVerify {
    @Serial
    private static final long serialVersionUID = -230533428543426025L;
    private Long id;
    private String commodityName;
    private Integer verifyStatus;
    private String verifyInfo;
    private String staffName;
    private String targetName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
