/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 后台应用程序启动入口
 *
 * @author berlin
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.hongyou.abner.data.mapper")
@ComponentScan(basePackages = {"com.hongyou.baron", "com.hongyou.abner"})
public class AbnerMainApplication {

    /**
     * 应用程序启动
     */
    public static void main(String[] args) {
        SpringApplication.run(AbnerMainApplication.class, args);
    }
}
