package com.hongyou.abner;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 后台应用程序启动入口
 *
 * @author berlin
 */
@SpringBootApplication
@MapperScan("com.hongyou.abner.data.mapper")
public class AbnerMainApplication {

    /**
     * 应用程序启动
     */
    public static void main(String[] args) {
        SpringApplication.run(AbnerMainApplication.class, args);
    }
}
