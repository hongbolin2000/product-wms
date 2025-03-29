/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.event;

/**
 * 系统消息日志
 *
 * @author Hong Bo Lin
 */
public interface EventLogManager {

    /**
     * 记录普通消息类型的日志
     *
     * @param event 日志对象
     */
    void info(EventLog event) throws IllegalAccessException;
}
