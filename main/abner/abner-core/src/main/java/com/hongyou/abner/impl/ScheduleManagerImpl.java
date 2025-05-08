/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.impl;

import cn.hutool.core.net.NetUtil;
import cn.hutool.cron.CronUtil;
import com.hongyou.abner.web.DataProvider;
import com.hongyou.abner.data.model.Schjob;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.schedule.Job;
import com.hongyou.baron.schedule.JobExecutionException;
import com.hongyou.baron.schedule.Schedule;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.schedule.ScheduleManager;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统任务
 *
 * @author Berlin
 */
@Component
public class ScheduleManagerImpl extends DataProvider implements ScheduleManager {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(ScheduleManagerImpl.class);

    /**
     * Spring上下文容器
     */
    private final ApplicationContext applicationContext;

    /**
     * 每个任务映射的接口类
     */
    private final HashMap<String, Job> jobClazz = new HashMap<>();

    /**
     * 注入依赖
     */
    public ScheduleManagerImpl(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 应用启动
     */
    @PostConstruct public void init() {
        // 加载数据库定义的任务
        List<Schjob> schjobs = this.db().schjob().list();
        Map<String, Schjob> jobs = schjobs.stream().collect(Collectors.toMap(Schjob::getJbname, i -> i));

        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(Schedule.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {

            // 加载任务注解
            Job jobClass = (Job) entry.getValue();
            Schedule schedule = jobClass.getClass().getAnnotation(Schedule.class);
            this.jobClazz.put(schedule.name(), jobClass);

            // 任务未定义直接跳过
            if (!jobs.containsKey(schedule.name())) {
                logger.warn("系统任务 ({}) 加载失败, 未注册在任务表中...", schedule.name());
                continue;
            }

            // 任务未启用
            Schjob job = jobs.get(schedule.name());
            if (ObjectUtil.equal(Schjob.ENABLD.No, job.getEnabld())) {
                logger.warn("系统任务 ({}) 加载失败, 任务未启用...", schedule.name());
                continue;
            }

            // 注册任务
            this.registry(job.getJbname(), job.getSccron());
        }
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    /**
     * 注册系统任务
     *
     * @param jobName 任务名称
     * @param cron 调度表达式
     */
    private void registry(final String jobName, final String cron) {
        if (this.jobClazz.containsKey(jobName)) {
            CronUtil.schedule(jobName, cron, () -> execute(jobName, this.jobClazz.get(jobName)));
            logger.info("系统任务 ({}) 注册成功...", jobName);
        } else {
            logger.warn("系统任务 ({}) 注册失败, 未映射到对应的运行程序...", jobName);
        }
    }

    /**
     * 执行任务
     *
     * @param jobName 任务名称
     * @param jobClass 任务接口
     */
    private void execute(final String jobName, final Job jobClass) {
        Date startAt = new Date();

        // 执行任务前
        Schjob schjob = this.db().schjob().getByName(jobName);
        if (schjob == null) {
            logger.error("系统任务 ({}) 执行失败, 未注册在任务表中...", jobName);
            return;
        }

        // 正在执行中
        if (Schjob.STATUS.Process.equals(schjob.getStatus())) {
            logger.error("系统任务 ({}) 执行失败, 任务正在执行中...", jobName);
            return;
        }
        schjob.status(Schjob.STATUS.Process).
                hostnm(NetUtil.getLocalHostName()).
                hostip(NetUtil.getIpByHost(schjob.getHostnm())).
                strttm(new Timestamp(startAt.getTime()));
        this.db().schjob().save(schjob);

        // 运行参数
        Map<String, Object> context = new HashMap<>();
        context.put("p1", schjob.getParam1());
        context.put("p2", schjob.getParam2());
        context.put("p3", schjob.getParam3());
        context.put("p4", schjob.getParam4());
        context.put("p5", schjob.getParam5());
        context.put("p6", schjob.getParam6());
        context.put("p7", schjob.getParam7());
        context.put("p8", schjob.getParam8());
        context.put("p9", schjob.getParam9());

        // 执行任务
        try {
            jobClass.execute(context);
            schjob.status(Schjob.STATUS.Success);
        } catch (JobExecutionException e) {
            schjob.status(Schjob.STATUS.Fail);
        }
        Date finishAt = new Date();

        // 任务执行完成
        schjob.fnshtm(new Timestamp(finishAt.getTime())).
                durtim(finishAt.getTime() - startAt.getTime());
        this.db().schjob().save(schjob);
    }

    /**
     * 移除系统任务
     *
     * @param jobName 任务名称
     */
    @Override
    public void remove(final String jobName) {
        CronUtil.remove(jobName);
        logger.info("系统任务 ({}) 移除成功...", jobName);
    }

    /**
     * 启用系统任务
     *
     * @param jobName 任务名称
     */
    @Override
    public void start(final String jobName, final String cron) {
        this.registry(jobName, cron);
    }

    /**
     * 运行系统任务
     *
     * @param jobName 任务名称
     */
    @Override
    public void run(String jobName) {
        if (this.jobClazz.containsKey(jobName)) {
            this.execute(jobName, this.jobClazz.get(jobName));
            logger.info("系统任务 ({}) 手动运行成功...", jobName);
        } else {
            logger.error("系统任务 ({}) 手动运行失败, 未映射到对应的运行程序...", jobName);
        }
    }
}
