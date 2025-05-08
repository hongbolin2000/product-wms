/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.web;

import cn.dev33.satoken.stp.StpUtil;
import com.hongyou.baron.web.event.EventLogManager;
import com.hongyou.abner.impl.EventLogManagerImpl;
import com.hongyou.baron.web.internation.InternationalManager;
import com.hongyou.abner.impl.InternationalManagerImpl;
import com.hongyou.baron.web.serial.SerialManager;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.baron.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注入系统数据库以及当前登录用户信息，功能代码继承此类可获取用户以及数据库组件
 * 此对象全局只有一个实例
 *
 * @author Berlin
 */
@Component
public class UserDataProvider extends DataProvider {

    /**
     * 系统消息日志管理器
     */
    protected EventLogManager eventLogManager;

    /**
     * 国际化语言
     */
    protected InternationalManager international;

    /**
     * 序列号生成器
     */
    protected SerialManager serialManager;

    /**
     * 获取当前登录的用户
     */
    protected Userms getLoginUser() {
        return this.db().userms().get(StpUtil.getLoginIdAsLong());
    }

    /**
     * 获取当前用户操作人
     */
    protected String getOperatorBy(final Userms userms) {
        if (StringUtil.isNotBlank(userms.getFulnam())) {
            return userms.getFulnam();
        }
        return userms.getUsernm();
    }

    /**
     * 注入数据库组件
     */
    @Autowired
    public void setEventLogManager(final EventLogManagerImpl eventLogManager) {
        this.eventLogManager = eventLogManager;
    }

    /**
     * 注入数据库国际化语言
     */
    @Autowired
    public void setInternationalManager(final InternationalManagerImpl international) {
        this.international = international;
    }

    /**
     * 注入序列号生成器
     */
    @Autowired
    public void setSerialManager(final SerialManager serialManager) {
        this.serialManager = serialManager;
    }
}
