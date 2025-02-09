/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.login;

import lombok.Data;

/**
 * @author Berlin
 */
@Data
public class LoginParam {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 加密密匙
     */
    private String key;

    /**
     * 7天内自动登录
     */
    private boolean autoLogin;

    /**
     * 验证码ID
     */
    private String captchaId;

    /**
     * 输入的验证码
     */
    private String captchaValue;
}
