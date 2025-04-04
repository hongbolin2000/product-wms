/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.internation;

import com.hongyou.abner.data.DataProvider;
import com.hongyou.abner.data.model.Tbfdds;
import com.hongyou.abner.data.model.VTbfdvl;
import com.hongyou.baron.cache.CacheUtil;
import com.hongyou.baron.cache.TimedCache;
import com.hongyou.baron.util.StringUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.RowUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.hongyou.abner.data.model.table.VTbfdvlTableDef.VTBFDVL;

/**
 * 数据库国际化语言
 *
 * @author Hong Bo Lin
 */
@Component
public class InternationalManagerImpl extends DataProvider implements InternationalManager {

    /**
     * 缓存表枚举显示值(7天未使用自动清除, key: 语言@表名, value: 枚举显示值)
     */
    private final TimedCache<String, Map<String, String>> tableValuesDisplay = CacheUtil.newTimedCache(
            1000 * 60 * 60 * 24 * 7
    );

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
        if (this.tableValuesDisplay.containsKey(key)) {
            return this.tableValuesDisplay.get(key);
        }

        // 查询表枚举值定义
        QueryWrapper wrapper = QueryWrapper.create();
        wrapper.select(VTBFDVL.FLDNAM.getName(), VTBFDVL.VALUE.getName(), VTBFDVL.DSPVAL.getName()).
                from(VTBFDVL.getName()).
                and(VTBFDVL.LANGUG.eq(local)).and(VTBFDVL.TBLNAM.eq(table)).
                orderBy(VTBFDVL.SORTNG.getName());
        List<VTbfdvl> vtbfdvls = RowUtil.toEntityList(Db.selectListByQuery(wrapper), VTbfdvl.class);

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
