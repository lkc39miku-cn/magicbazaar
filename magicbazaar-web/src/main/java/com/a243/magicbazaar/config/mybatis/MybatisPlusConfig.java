package com.a243.magicbazaar.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus
 * 配置MybatisPlus的功能选项
 */
@Configuration
@MapperScan(value = {"com.a243.magicbazaar.dao.mapper", "com.a243.magicbazaar.util.scheduled.sql"})
public class MybatisPlusConfig {
    /**
     * 分页插件
     *
     * @return 过滤器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
