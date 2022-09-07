package com.a243.magicbazaar.util.scheduled.sql;

import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface CommoditySql {

    // 预售商品更新 发售时间上架为现货
    @Update(value = "update magicbazaar.mb_commodity set publish_status = 1,preview_end_time = null,preview_info = null, sale_start_time = null,commodity_status = 1" +
            " where" +
            " id in" +
            " (select mcmcvi.id from (select mb_commodity.id from mb_commodity, magicbazaar.mb_commodity_verify where verify_status = 1 and DATE(now()) >= sale_start_time) as mcmcvi)")
    void on();

    // 预售商品更新 截止时间下架
    @Update(value = "update magicbazaar.mb_commodity set mb_commodity.publish_status = 0" +
            " where" +
            " id in" +
            " (select mcmcvi.id from (select mb_commodity.id from mb_commodity, magicbazaar.mb_commodity_verify where verify_status = 1 and DATE(now()) >= preview_end_time and DATE(now()) < sale_start_time) as mcmcvi)")
    void off();

    // 促销时间结束 修改商品促销状态
    @Update(value = "update magicbazaar.mb_commodity set promotion_type = 0,promotion_price = null,promotion_start_time = null,preview_end_time = null" +
            " where" +
            " id in" +
            " (select mcmcvi.id from (select mb_commodity.id from mb_commodity, magicbazaar.mb_commodity_verify where verify_status = 1 and now() > promotion_end_time) as mcmcvi)")
    void saleOff();
}
