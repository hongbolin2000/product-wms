/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.authentication;

import com.hongyou.baron.security.Authenticator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/")
public class LoginPage {

    /**
     * 用户认证器
     */
    private final Authenticator authenticator;

    /**
     * 构造函数
     */
    public LoginPage(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public boolean login(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password
    ) {
        return this.authenticator.login(username, password);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public boolean logout() {
        return this.authenticator.logout();
    }
}
