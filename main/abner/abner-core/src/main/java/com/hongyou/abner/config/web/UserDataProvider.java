/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.web;

import cn.dev33.satoken.stp.StpUtil;
import com.hongyou.abner.data.DataProvider;
import com.hongyou.abner.data.model.Userms;

import java.sql.Timestamp;

/**
 * 注入系统数据库以及当前登录用户信息，功能代码继承此类可获取用户以及数据库组件
 *
 * @author Berlin
 */
public class UserDataProvider extends DataProvider {

    /**
     * 当前登录的用户
     */
    private Userms userms;

    /**
     * 获取当前登录用户ID
     */
    protected Long getLoginUserId() {
        return StpUtil.getLoginIdAsLong();
    }

    /**
     * 获取当前登录用户名
     */
    protected String getLoginUserName() {
        return this.getLoginUser().getUsernm();
    }

    /**
     * 获取当前登录的用户
     */
    protected Userms getLoginUser() {
        if (this.userms == null) {
            this.userms = this.db().userms().get(getLoginUserId());
        }
        return this.userms;
    }

    /**
     * 获取当前登录用户公司ID
     */
    protected Long getUserCompanyId() {
        return this.getLoginUser().getCmpnid();
    }

    /**
     * 获取当前时间
     */
    protected Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }
}
