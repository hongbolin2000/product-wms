/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.login;

import lombok.Builder;
import lombok.Data;

/**
 * @author Berlin
 */
@Data
@Builder
public class LoginResult {

    /**
     * 编号
     */
    private int loginCode;

    /**
     * 消息
     */
    private String message;

    /**
     * 是否被冻结
     */
    private boolean isFrozen;

    /**
     * 是否已登录
     */
    private boolean isLogin;
}
