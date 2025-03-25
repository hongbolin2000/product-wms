/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
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
    public void setMOMDB(WMSDB wmsdb) {
        this.wmsdb = wmsdb;
    }

    /**
     * 获取数据库组件
     */
    public WMSDB db() {
        return wmsdb;
    }
}
