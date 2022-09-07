package com.a243.magicbazaar.util.scheduled.sql;

import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSql {
    // 禁言解禁用户

    @Update(value = "update magicbazaar.mb_user_comment_status set comment_status = 0,comment_info = null,comment_start_time = null,comment_end_time = null,staff_id = null" +
            " where now() > comment_end_time")
    void on();
}
