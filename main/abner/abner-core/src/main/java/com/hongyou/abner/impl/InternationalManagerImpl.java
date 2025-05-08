/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.impl;

import com.hongyou.abner.web.DataProvider;
import com.hongyou.abner.data.model.Tbfdds;
import com.hongyou.abner.data.model.VTbfdvl;
import com.hongyou.baron.Application;
import com.hongyou.baron.cache.CacheUtil;
import com.hongyou.baron.cache.TimedCache;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.internation.InternationalManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.hongyou.abner.data.model.table.VTbfdvlTableDef.VTBFDVL;

/**
 * 数据库国际化语言
 *
 * @author Berlin
 */
@Service
public class InternationalManagerImpl extends DataProvider implements InternationalManager {

    /**
     * 缓存表枚举显示值(7天未使用自动清除, key: 语言@表名, value: 枚举显示值)
     */
    private final TimedCache<String, Map<String, String>> tableValuesDisplay = CacheUtil.newTimedCache(
            1000 * 60 * 60 * 24 * 7
    );

    /**
     * 应用配置
     */
    private Application application;

    /**
     * 注入应用配置
     */
    @Autowired
    public void setApplication(final Application application) {
        this.application = application;
    }

    /**
     * 加载表字典值显示值
     *
     * @param request http request
     * @param table 表名
     * @return key为字段为@枚举值，value为显示值
     */
    @Override
    public Map<String, String> getTableValuesDisplay(final HttpServletRequest request, final String table) {
        String local = this.getInternationLocal(request);
        String key = local + "@" + table;

        // 从缓存中读取
        if (!this.application.isDebug() && this.tableValuesDisplay.containsKey(key)) {
            return this.tableValuesDisplay.get(key);
        }

        // 查询表枚举值定义
        List<VTbfdvl> vtbfdvls = new VTbfdvl().
                select(VTBFDVL.FLDNAM.getName(), VTBFDVL.VALUE.getName(), VTBFDVL.DSPVAL.getName()).
                and(VTBFDVL.LANGUG.eq(local)).and(VTBFDVL.TBLNAM.eq(table)).
                orderBy(VTBFDVL.SORTNG.getName()).
                list();

        Map<String, String> displays = new LinkedHashMap<>();
        vtbfdvls.forEach(vtbfdvl -> displays.put(
                vtbfdvl.getFldnam() + "@" + vtbfdvl.getValue(), vtbfdvl.getDspval()
        ));
        this.tableValuesDisplay.put(key, displays);
        return displays;
    }

    /**
     * 从http请求中获取语言
     *
     * @param request http request
     */
    private String getInternationLocal(final HttpServletRequest request) {
        String local = request.getHeader("local");
        local = StringUtil.blankToDefault(local, Tbfdds.LANGUG.Chinese);
        return local;
    }
}
