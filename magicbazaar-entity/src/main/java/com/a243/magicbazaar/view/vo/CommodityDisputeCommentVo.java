package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.CommodityDisputeComment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CommodityDisputeCommentVo extends CommodityDisputeComment {
    @Serial
    private static final long serialVersionUID = -1072620129758243117L;
    private Long id;
    private String commodityDisputeInfoNumber;
    private String userAvatar;
    private String userName;
    private String staffName;
    private String context;
    private String contextImg;
    private String replyContext;
    private String replyContextImg;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
