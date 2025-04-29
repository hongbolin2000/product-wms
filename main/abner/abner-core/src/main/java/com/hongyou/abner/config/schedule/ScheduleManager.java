/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.schedule;

/**
 * 系统任务
 *
 * @author Berlin
 */
public interface ScheduleManager {

    /**
     * 移除系统任务
     *
     * @param jobName 任务名称
     */
    void remove(String jobName);

    /**
     * 启用系统任务
     *
     * @param jobName 任务名称
     */
    void start(String jobName, String cron);

    /**
     * 运行系统任务
     *
     * @param jobName 任务名称
     */
    void run(String jobName);
}
