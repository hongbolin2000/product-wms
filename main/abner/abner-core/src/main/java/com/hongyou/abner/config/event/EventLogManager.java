/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.event;

/**
 * 系统消息日志
 *
 * @author Berlin
 */
public interface EventLogManager {

    /**
     * 记录普通消息类型的日志
     *
     * @param event 日志对象
     */
    void info(EventLog event) throws IllegalAccessException;

    /**
     * 记录危险消息类型的日志
     *
     * @param event 日志对象
     */
    void critical(EventLog event) throws IllegalAccessException;
}
