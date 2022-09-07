package com.a243.magicbazaar.view.param;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserCommentStatusParam {
    private PageParam pageParam;
    private String id;
    private String username;
    private String commentStatus;
    private String commentInfo;
    private String comment_info;
    private String staffName;

    private LocalDateTime commentStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime comment_start_time;

    private LocalDateTime commentEndTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime comment_end_time;
}
