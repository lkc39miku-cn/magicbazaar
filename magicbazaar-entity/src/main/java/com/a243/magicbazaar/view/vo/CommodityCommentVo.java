package com.a243.magicbazaar.view.vo;

import com.a243.magicbazaar.dao.entity.CommodityComment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CommodityCommentVo extends CommodityComment {

    @Serial
    private static final long serialVersionUID = 566492905181719760L;

    private Long id;
    private String commodityName;
    private Long commodityId;
    private String commodityPhoto;
    private String userName;
    private String userPhoto;
    private String context;
    private String contextImg;
    private String toUserName;
    private Integer publishStatus;
    private Integer deleteStatus;
    private Long parentId;
    private List<CommodityCommentVo> child;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
