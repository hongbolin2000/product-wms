/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
