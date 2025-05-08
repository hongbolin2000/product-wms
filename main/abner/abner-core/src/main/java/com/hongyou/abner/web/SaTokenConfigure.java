/*
 * Copyright 2024, Hongyou Software Development Studio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hongyou.abner.web;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.hongyou.baron.exceptions.NotAuthorizedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token配置类
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    /**
     * 初始化Sa-Token配置
     */
    @Bean
    @Primary
    public SaTokenConfig getSaTokenConfigPrimary() {
        SaTokenConfig config = new SaTokenConfig();

        // token/cookie的名称
        config.setTokenName("authorization");
        // token有效期24小时(秒)
        config.setTimeout(60 * 60 * 24L);
        // token最低活跃评率30分钟(秒)
        config.setActiveTimeout(60 * 30L);
        // 开启动态活跃频率设置（7天免登录时动态设置）
        config.setDynamicActiveTimeout(true);
        // token生成风格
        config.setTokenStyle("random-128");
        // 是否自动续签token
        config.setAutoRenew(false);
        // token前缀
        config.setTokenPrefix("Hongyou");

        // 是否从header中读取token
        config.setIsReadHeader(true);
        // 是否将token写入到header中
        config.setIsWriteHeader(true);
        // 是否从body中读取token
        config.setIsReadBody(false);
        // 是否从cookie中读取token
        config.setIsReadCookie(false);
        return config;
    }

    /**
     * 注册认证拦截器
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        // 注册Sa-Token拦截器，拦截需要登录的请求。
        InterceptorRegistration interceptor = registry.addInterceptor(new SaInterceptor(handle -> {
            try {
                StpUtil.checkLogin();
                StpUtil.updateLastActiveToNow();
            } catch (Exception e) {
                throw new NotAuthorizedException("User not authentication");
            }
        }));
        interceptor.addPathPatterns("/**");
        interceptor.excludePathPatterns("/auth/**", "/doc/image", "/doc/download");
    }
}
