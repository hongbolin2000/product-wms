/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.web;

import cn.dev33.satoken.stp.StpUtil;
import com.hongyou.abner.config.event.EventLogManager;
import com.hongyou.abner.config.event.EventLogManagerImpl;
import com.hongyou.abner.config.internation.InternationalManager;
import com.hongyou.abner.config.internation.InternationalManagerImpl;
import com.hongyou.abner.data.DataProvider;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.baron.util.StringUtil;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.hongyou.abner.data.model.table.CmpnmsTableDef.CMPNMS;

/**
 * 注入系统数据库以及当前登录用户信息，功能代码继承此类可获取用户以及数据库组件
 * 此对象全局只有一个实例
 *
 * @author Berlin
 */
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
        return this.db().userms().get(getLoginUserId());
    }

    /**
     * 获取当前登录用户公司ID
     */
    protected Long getUserCompanyId() {
        return this.getLoginUser().getCmpnid();
    }

    /**
     * 获取当前用户操作人
     */
    protected String getOperatorBy() {
        Userms userms = this.getLoginUser();
        if (StringUtil.isNotBlank(userms.getFulnam())) {
            return userms.getFulnam();
        }
        return this.getLoginUserName();
    }

    /**
     * 获取当前时间
     */
    protected Timestamp getCurrentTime() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select(QueryMethods.currentTimestamp()).from(CMPNMS);
        Row row = Db.selectOneByQuery(wrapper);
        LocalDateTime localDateTime = row.getLocalDateTime("CURRENT_TIMESTAMP()");
        return Timestamp.valueOf(localDateTime);
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
}
