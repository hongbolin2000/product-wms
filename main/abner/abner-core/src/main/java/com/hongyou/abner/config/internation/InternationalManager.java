/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.internation;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * 数据库国际化语言
 *
 * @author Hong Bo Lin
 */
public interface InternationalManager {

    /**
     * 加载表字典值显示值
     *
     * @param request http request
     * @param table 表名
     * @return key为字段为@枚举值，value为显示值
     */
    Map<String, String> getTableValuesDisplay(HttpServletRequest request,String table);
}
