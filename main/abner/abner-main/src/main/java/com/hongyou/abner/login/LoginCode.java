/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.login;

import lombok.Getter;

/**
 * @author Berlin
 */
@Getter
public enum LoginCode {

    /**
     * 登录成功
     */
    LG200(200),

    /**
     * 用户名为空
     */
    LG001(-1),

    /**
     * 密码为空
     */
    LG002(-2),

    /**
     * 用户名不存在
     */
    LG003(-3);

    /**
     * 枚举值
     */
    private final int value;

    /**
     * 构造函数
     */
    LoginCode(int value) {
        this.value = value;
    }
}
