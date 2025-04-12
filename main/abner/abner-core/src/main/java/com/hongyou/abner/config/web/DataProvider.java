/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.config.web;

import com.hongyou.abner.data.WMSDB;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.hongyou.abner.data.model.table.CmpnmsTableDef.CMPNMS;

/**
 * 注入数据库组件，只需要集成此组件即可操作数据库对象
 *
 * @author Berlin
 */
@Component
public class DataProvider {

    /**
     * 数据库组件
     */
    private WMSDB wmsdb;

    /**
     * 注入数据库组件
     */
    @Autowired
    public void setMOMDB(final WMSDB wmsdb) {
        this.wmsdb = wmsdb;
    }

    /**
     * 获取数据库组件
     */
    public WMSDB db() {
        return wmsdb;
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
}
