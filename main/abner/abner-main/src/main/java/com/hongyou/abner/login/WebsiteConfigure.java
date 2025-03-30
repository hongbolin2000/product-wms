/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.login;

import lombok.Builder;
import lombok.Data;

/**
 * 登录框架属性配置
 *
 * @author Berlin
 */
@Data
@Builder
public class WebsiteConfigure {

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 网站标题
     */
    private String websiteTitle;

    /**
     * 网站副标题
     */
    private String websiteSubtitle;

    /**
     * 是否打开验证码
     */
    private boolean captchaVerify;

    /**
     * 是否允许自动登录
     */
    private boolean autoLogin;

    /**
     * 是否允许记住账号
     */
    private boolean rememberAccount;

    /**
     * 是否允许记住密码
     */
    private boolean rememberPassword;

    /**
     * 用户昵称
     */
    private String nikeName;

    /**
     * 用户头像
     */
    private String avatar;
}
