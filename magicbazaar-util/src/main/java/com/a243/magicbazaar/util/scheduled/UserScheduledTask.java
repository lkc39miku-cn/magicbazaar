package com.a243.magicbazaar.util.scheduled;

import com.a243.magicbazaar.util.scheduled.sql.UserSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Async
public class UserScheduledTask {
    private final UserSql userSql;

    @Autowired
    public UserScheduledTask(UserSql userSql) {
        this.userSql = userSql;
    }

    @Scheduled(fixedDelay = 10000)
    public void commodity() {
        userSql.on();
    }
}
