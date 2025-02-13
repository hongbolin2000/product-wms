/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.login;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 登录框架属性配置
 *
 * @author Berlin
 */
@Data
@Component
@ConfigurationProperties(prefix = "hongyou.login")
public class LoginProperties {

    /**
     * 是否打开验证
     */
    private boolean captchaVerify = true;

    /**
     * 是否允许自动登录
     */
    private boolean allowAutoLogin = true;

    /**
     * 是否允许记住账号
     */
    private boolean allowRememberAccount = true;

    /**
     * 是否允许记住密码
     */
    private boolean allowRememberPassword = true;
}
