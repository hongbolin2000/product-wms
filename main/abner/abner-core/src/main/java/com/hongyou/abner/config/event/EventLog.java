/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.event;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 日志对象
 *
 * @author Hong Bo Lin
 */
@Data
@Builder
public class EventLog {

    /**
     * 公司ID
     */
    private Long domain;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 模块号
     */
    private String module;

    /**
     * 模块描述
     */
    private String name;

    /**
     * 操作动作
     */
    private String action;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 原始值对象
     */
    private Object oldValue;

    /**
     * 新值对象
     */
    private Object newValue;

    /**
     * 表枚举值
     */
    private Map<String, String> enumsDisplay;
}
