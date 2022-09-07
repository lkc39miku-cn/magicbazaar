package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.UserStars;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UserStarsVo extends UserStars {
    @Serial
    private static final long serialVersionUID = -7115626119398013279L;
    private Long id;
    private String username;
    private String userPhoto;
    private String commodityName;
    private BigDecimal stars;
    private String commentInfo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
