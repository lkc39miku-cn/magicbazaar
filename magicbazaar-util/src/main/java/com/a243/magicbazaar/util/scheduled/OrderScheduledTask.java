package com.a243.magicbazaar.util.scheduled;

import com.a243.magicbazaar.util.scheduled.sql.OrderSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Async
public class OrderScheduledTask {
    private final OrderSql orderSql;

    @Autowired
    public OrderScheduledTask(OrderSql orderSql) {
        this.orderSql = orderSql;
    }

    @Scheduled(fixedDelay = 10000)
    public void commodity() {
        orderSql.timeout();
        orderSql.timeout1();
    }
}
