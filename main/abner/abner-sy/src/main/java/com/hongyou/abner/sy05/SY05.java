/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy05;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.schedule.ScheduleManager;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Schjob;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

/**
 * 系统任务
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/sy05")
public class SY05 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(SY05.class);

    /**
     * 系统任务管理器
     */
    private final ScheduleManager scheduleManager;

    /**
     * 注入依赖
     */
    public SY05(final ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
    }

    /**
     * 禁用任务
     */
    @PostMapping("/disable")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry disable(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            for (Long id: ids) {
                Schjob schjob = this.db().schjob().get(id);
                boolean disabled = Schjob.ENABLD.No.equals(schjob.getEnabld());
                String action = disabled ? "启用" : "禁用";

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(SY05.class.getSimpleName()).
                        name("系统任务").
                        action(action).
                        message(StringUtil.format("系统任务[{}]{}成功", schjob.getJbname(), action)).
                        build();
                this.eventLogManager.info(event);

                schjob.enabld(disabled ? Schjob.ENABLD.Yes : Schjob.ENABLD.No).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().schjob().save(schjob);

                // 启动/移除系统任务
                if (disabled) {
                    this.scheduleManager.start(schjob.getJbname(), schjob.getSccron());
                } else {
                    this.scheduleManager.remove(schjob.getJbname());
                }
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("系统任务禁用失败", e);
            throw new RestRuntimeException("系统任务禁用失败");
        }
    }

    /**
     * 运行任务
     */
    @PostMapping("/run")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry run(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);

            for (Long id: ids) {
                Schjob schjob = this.db().schjob().get(id);

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(SY05.class.getSimpleName()).
                        name("系统任务").
                        action("手动运行").
                        message(StringUtil.format("系统任务[{}]手动运行成功", schjob.getJbname())).
                        build();
                this.eventLogManager.info(event);

                this.scheduleManager.run(schjob.getJbname());
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("系统任务运行失败", e);
            throw new RestRuntimeException("系统任务运行失败");
        }
    }
}
