package com.a243.magicbazaar.util.scheduled;

import com.a243.magicbazaar.util.scheduled.sql.CommoditySql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Async
public class CommodityScheduledTask {

    private final CommoditySql commoditySql;

    @Autowired
    public CommodityScheduledTask(CommoditySql commoditySql) {
        this.commoditySql = commoditySql;
    }

    @Scheduled(fixedDelay = 10000)
    public void commodity() {
        commoditySql.on();
        commoditySql.off();
        commoditySql.saleOff();
    }
}
