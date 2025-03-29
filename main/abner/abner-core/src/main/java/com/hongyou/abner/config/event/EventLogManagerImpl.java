/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.event;

import com.hongyou.abner.data.DataProvider;
import com.hongyou.abner.data.model.Evlghd;
import com.hongyou.abner.data.model.Evlgvl;
import com.hongyou.baron.Reference;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统消息日志管理器
 *
 * @author Hong Bo Lin
 */
@Service
public class EventLogManagerImpl extends DataProvider implements EventLogManager {

    /**
     * 记录普通消息类型的日志
     *
     * @param event 日志对象
     */
    @Override
    public void info(final EventLog event) throws IllegalAccessException {
        this.save(event, Evlghd.EVTLVL.Info);
    }

    /**
     * 生成日志消息
     *
     * @param event 日志对象
     * @param level 日志等级
     */
    private void save(final EventLog event, final String level) throws IllegalAccessException {
        Evlghd evlghd = new Evlghd();
        evlghd.cmpnid(event.getDomain()).
                module(event.getModule()).
                modnam(event.getName()).
                action(event.getAction()).
                evtlvl(level).
                mesage(StringUtil.blankToDefault(event.getMessage(), StringUtil.EMPTY)).
                oprtby(event.getOperator());
        this.db().evlghd().save(evlghd);

        // 消息日志属性值列表
        if (event.getNewValue() != null) {
            List<Evlgvl> eventValues = this.toEventValues(event);
            eventValues.forEach(evlgvl -> {
                evlgvl.elhdid(evlghd.getElhdid()).
                        oprtby(event.getOperator());
                this.db().evlgvl().save(evlgvl);
            });
        }
    }

    /**
     * 将两个属性值对象转换为日志属性值列表
     * <p>通过Java类字段设置的Reference注解生成日志属性值列表</p>
     *
     * @param event 日志对象
     */
    private List<Evlgvl> toEventValues(final EventLog event) throws IllegalAccessException {
        List<Evlgvl> evlgvls = new ArrayList<>();
        for (Field field : event.getNewValue().getClass().getDeclaredFields()) {

            // 检查是否配置了注解
            if (!field.isAnnotationPresent(Reference.class)) {
                continue;
            }
            // Long作为ID、Timestamp作为操作时间不记录日志
            if (field.getType() == Long.class || field.getType() == Timestamp.class) {
                continue;
            }
            // 设置字段可访问性
            field.setAccessible(true);

            // 新值
            String filedNewValue = field.get(event.getNewValue()).toString();
            if (event.getOldValue() == null && StringUtil.isBlank(filedNewValue)) {
                continue;
            }
            filedNewValue = this.getEnumDisplay(event.getEnumsDisplay(), field, filedNewValue);

            // 如果传入的原始值对象，则对比两个字段是否一致
            String filedOldValue = "";
            if (event.getOldValue() != null) {
                filedOldValue = field.get(event.getOldValue()).toString();
                filedOldValue = this.getEnumDisplay(event.getEnumsDisplay(), field, filedOldValue);
                if (ObjectUtil.equal(filedNewValue, filedOldValue)) {
                    continue;
                }
            }

            // 生成日志属性值
            Reference reference = field.getAnnotation(Reference.class);
            Evlgvl evlgvl = new Evlgvl();
            evlgvl.elhdid(null).
                    elprky(reference.alias()).
                    elprnm(reference.secondary()).
                    elodvl(filedOldValue).
                    elprvl(filedNewValue);
            evlgvls.add(evlgvl);
        }
        return evlgvls;
    }

    /**
     * 枚举显示值
     *
     * @param enumsDisplay 枚举限制定义
     * @param field 字段
     * @param value 值
     */
    private String getEnumDisplay(
            final Map<String, String> enumsDisplay, final Field field, final String value
    ) {
        if (enumsDisplay == null) {
            return value;
        }
        if (enumsDisplay.containsKey(field.getName().toUpperCase() + '@' + value)) {
            return enumsDisplay.get(field.getName().toUpperCase() + '@' + value);
        }
        return value;
    }
}
