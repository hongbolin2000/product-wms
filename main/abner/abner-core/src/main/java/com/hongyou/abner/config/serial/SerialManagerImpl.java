/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.serial;

import com.hongyou.abner.config.web.DataProvider;
import com.hongyou.abner.data.model.Srgnhd;
import com.hongyou.abner.data.model.Srgnln;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.DateUtil;
import com.hongyou.baron.util.StringUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 序列号生成
 *
 * @author Berlin
 */
@Service
public class SerialManagerImpl extends DataProvider implements SerialManager {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(SerialManagerImpl.class);

    /**
     * 生成单个序列号
     *
     * @param serialType 生成类型
     * @param appKey 应用键值
     */
    @Override
    public String get(final String serialType, final String appKey) {

        Srgnhd srgnhd = this.db().srgnhd().getByType(serialType);
        if (srgnhd == null) {
            logger.error("未找到定义的序列号生成类型: {}", serialType);
            return null;
        }

        Timestamp currentTime = this.getCurrentTime();
        StringBuilder builder = new StringBuilder(srgnhd.getSrprfx());

        // 生成日期序列号
        String serialKey = "";
        if (Srgnhd.SRGSTG.DATE.equals(srgnhd.getSrgstg())) {
            serialKey = DateUtil.format(
                    currentTime, StringUtil.blankToDefault(srgnhd.getSrptrn(), "yyMMdd")
            );
        }
        builder.append(serialKey);

        // 补足长度
        String serialNo = String.format("%-" + srgnhd.getSrllen() + "s", "").replace(' ', '0');
        builder.append(serialNo);

        // 生成序列号
        Srgnln srgnln = this.db().srgnln().getByAppKey(srgnhd.getSghdid(), appKey, serialKey);
        if (srgnln == null) {
            srgnln = new Srgnln();
            srgnln.sghdid(srgnhd.getSghdid()).
                    appkey(appKey).
                    srlkey(serialKey).
                    curseq(0L).
                    oprttm(currentTime);
        }
        srgnln.curseq(srgnln.getCurseq() + 1);

        builder.append(srgnln.getCurseq() + 1);
        return builder.toString();
    }
}
