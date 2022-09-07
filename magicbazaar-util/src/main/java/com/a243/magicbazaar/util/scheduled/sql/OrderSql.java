package com.a243.magicbazaar.util.scheduled.sql;

import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSql {

    // 订单超时
    @Update(value = "update magicbazaar.mb_order, magicbazaar.mb_order_info set mb_order.order_type_id = 16" +
            " where" +
            " mb_order.create_time < date_sub(now(), interval 1 day) and" +
            " mb_order.id in" +
            " (select mcmcvi.order_id from (select mb_order_info.order_id from magicbazaar.mb_order_info where mb_order_info.order_type_id = 1) as mcmcvi)")
    void timeout();


    @Update(value = "update magicbazaar.mb_order_info set order_type_id = 16" +
            " where" +
            " order_id in" +
            " (select mcmcvi.id from (select mb_order.id from magicbazaar.mb_order where mb_order.order_type_id = 16) as mcmcvi)")
    void timeout1();
}
