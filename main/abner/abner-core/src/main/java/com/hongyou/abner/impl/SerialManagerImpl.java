/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.impl;

import com.hongyou.abner.web.DataProvider;
import com.hongyou.abner.data.model.Srgnhd;
import com.hongyou.abner.data.model.Srgnln;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.DateUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.serial.SerialManager;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
     * @param applicationKey 应用键值
     */
    @Override
    public String get(final String serialType, final String applicationKey) {
        return this.get(serialType, applicationKey, 1).get(0);
    }

    /**
     * 批量生成序列号
     *
     * @param serialType 生成类型
     * @param applicationKey 应用键值
     */
    @Override
    public List<String> get(final String serialType, final String applicationKey, final int count) {
        synchronized (serialType.intern()) {
            Srgnhd srgnhd = this.db().srgnhd().getByType(serialType);
            if (srgnhd == null) {
                logger.error("未找到定义的序列号生成类型: {}", serialType);
                return null;
            }

            Timestamp currentTime = this.getCurrentTime();
            StringBuilder headBuilder = new StringBuilder(srgnhd.getSrprfx());

            // 生成日期序列号
            String serialKey = "";
            if (Srgnhd.SRGSTG.Date.equals(srgnhd.getSrgstg())) {
                serialKey = DateUtil.format(
                        currentTime, StringUtil.blankToDefault(srgnhd.getSrptrn(), "yyMMdd")
                );
            }
            headBuilder.append(serialKey);

            // 生成序列号
            Srgnln srgnln = this.db().srgnln().getByAppKey(srgnhd.getSghdid(), applicationKey, serialKey);
            if (srgnln == null) {
                srgnln = new Srgnln();
                srgnln.sghdid(srgnhd.getSghdid()).
                        appkey(applicationKey).
                        srlkey(serialKey).
                        curseq(0L).
                        oprttm(currentTime);
            }
            List<String> serialNos = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                String serialNo = headBuilder.toString();

                // 当前序列号
                Long currentSequence = srgnln.getCurseq() + 1;
                srgnln.curseq(currentSequence);

                // 补足长度
                int repairLength = srgnhd.getSrllen() - serialNo.length() - String.valueOf(currentSequence).length();
                if (repairLength > 0) {
                    String repairNo = String.format("%-" + repairLength + "s", "").replace(' ', '0');
                    serialNo += repairNo;
                }

                serialNo += currentSequence;
                serialNos.add(serialNo);
            }
            this.db().srgnln().save(srgnln);
            return serialNos;
        }
    }
}
