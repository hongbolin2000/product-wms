/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.authentication;

import com.hongyou.abner.data.DataProvider;
import com.hongyou.abner.data.model.Oprtms;
import com.hongyou.baron.security.AuthorizeUser;
import com.hongyou.baron.security.UserLoader;
import org.springframework.stereotype.Service;

/**
 * 用户加载器
 *
 * @author Berlin
 */
@Service
public class AuthenticationUserLoader extends DataProvider implements UserLoader {

    /**
     * 通过用户名加载用户信息信息
     *
     * @param username 用户名
     */
    @Override
    public AuthorizeUser loadByUserName(String username) {
        Oprtms oprtms = this.db().oprtms().getByAccount(username);
        if (oprtms == null) {
            return null;
        }
        return AuthorizeUser.builder().
                username(oprtms.getAccunt()).
                password(oprtms.getPaswrd()).
                accountExpireTime(null).
                accountUnlock(true).
                credentialsExpireTime(null).
                enabled(true).
                build();
    }
}
