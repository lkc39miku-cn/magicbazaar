package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.UserCommentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UserCommentStatusVo extends UserCommentStatus {
    @Serial
    private static final long serialVersionUID = 6909019280681203706L;
    private Long id;
    private String userName;
    private Integer commentStatus;
    private String commentInfo;
    private LocalDateTime commentStartTime;
    private LocalDateTime commentEndTime;
    private String staffName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
