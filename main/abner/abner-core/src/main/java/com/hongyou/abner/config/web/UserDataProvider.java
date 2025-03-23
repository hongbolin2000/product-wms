/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.web;

import cn.dev33.satoken.stp.StpUtil;
import com.hongyou.abner.data.DataProvider;
import com.hongyou.abner.data.model.Oprtms;

/**
 * 注入系统数据库以及当前登录用户信息，功能代码继承此类可获取用户以及数据库组件
 *
 * @author Hong Bo Lin
 */
public class UserDataProvider extends DataProvider {

    /**
     * 当前登录的用户
     */
    private Oprtms oprtms;

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
        return this.getLoginUser().getAccunt();
    }

    /**
     * 获取当前登录的用户
     */
    protected Oprtms getLoginUser() {
        if (this.oprtms == null) {
            this.oprtms = this.db().oprtms().get(getLoginUserId());
        }
        return this.oprtms;
    }

    /**
     * 获取当前登录用户公司ID
     */
    protected Long getUserCompanyId() {
        return this.getLoginUser().getCmpnid();
    }
}
