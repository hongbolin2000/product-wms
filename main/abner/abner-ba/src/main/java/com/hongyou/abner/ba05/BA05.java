/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.ba05;

import com.hongyou.baron.web.event.EventLog;
import com.hongyou.abner.web.UserDataProvider;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.model.VWklghd;
import com.hongyou.abner.data.model.Wklghd;
import com.hongyou.abner.data.model.Wklgln;
import com.hongyou.abner.data.pojo.WklglnPojo;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 工作日志
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/ba05")
public class BA05 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(BA05.class);

    /**
     * 保存工作日志
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(@RequestBody final WorkingLogPojo pojo) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Wklghd wklghd = null; VWklghd oldVWklghd = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                wklghd = this.db().wklghd().get(pojo.getId());
                oldVWklghd = new VWklghd().wklgid(wklghd.getWklgid()).oneById();
            }

            // 新增
            if (ObjectUtil.isNull(wklghd)) {
                wklghd = new Wklghd();
                wklghd.userid(loginUser.getUserid()).
                        repttm(currentTime);
            }

            wklghd.projid(pojo.getProjectId()).
                    wkcont(pojo.getWorkingContent()).
                    strdat(pojo.getStartDate()).
                    enddat(pojo.getEndDate()).
                    strtim(pojo.getStartTime()).
                    endtim(pojo.getEndTime()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().wklghd().save(wklghd);
            VWklghd vWklghd = new VWklghd().wklgid(wklghd.getWklgid()).oneById();

            // 记录日志
            String action = oldVWklghd == null ? "填报" : "修改";
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(BA05.class.getSimpleName()).
                    name("工作日志").
                    action(action).
                    message(StringUtil.format("工作日志[{}]{}成功", wklghd.getWkcont(), action)).
                    oldValue(oldVWklghd).
                    newValue(vWklghd).
                    build();
            this.eventLogManager.info(event);

            // 已经存在的工作日志明细id
            Set<Long> wklnids = this.db().wklgln().listByWorkLog(wklghd.getWklgid()).stream().
                    map(Wklgln::getWklnid).collect(Collectors.toSet());
            for (WklglnPojo line : pojo.getLogLines()) {

                // 修改
                Wklgln wklgln = null; Wklgln oldWklgln = null;
                if (ObjectUtil.isNotNull(line.getId())) {
                    wklgln = this.db().wklgln().get(line.getId());
                    oldWklgln = (Wklgln) wklgln.clone();
                    wklnids.remove(line.getId());
                }

                // 新增
                if (ObjectUtil.isNull(wklgln)) {
                    wklgln = new Wklgln();
                    wklgln.wklgid(wklghd.getWklgid());
                }

                wklgln.wrkimg(line.getWorkingImage()).
                        wkcont(line.getWorkingContent()).
                        strtim(line.getStartTime()).
                        endtim(line.getEndTime()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().wklgln().save(wklgln);

                // 记录日志
                action = oldWklgln == null ? "填报" : "修改";
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(BA05.class.getSimpleName()).
                        name("工作日志").
                        action(action + "日志明细").
                        message(StringUtil.format("工作日志[{}] 日志明细[{}]{}成功",
                                wklghd.getWkcont(), wklgln.getWkcont(), action)
                        ).
                        oldValue(oldWklgln).
                        newValue(wklgln).
                        build();
                this.eventLogManager.info(event);
            }

            // 删除工作日志明细
            for (Long wklnid : wklnids) {
                Wklgln wklgln = this.db().wklgln().get(wklnid);

                // 记录日志
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(BA05.class.getSimpleName()).
                        name("工作日志").
                        action("删除日志明细").
                        message(StringUtil.format("工作日志[{}] 日志明细[{}]删除成功",
                                wklghd.getWkcont(), wklgln.getWkcont())
                        ).
                        newValue(wklgln).
                        build();
                this.eventLogManager.critical(event);
                this.db().wklgln().delete(wklgln);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("工作日志填报失败", e);
            throw new RestRuntimeException("工作日志填报失败");
        }
    }

    /**
     * 删除工作日志
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);

            for (Long id: ids) {
                Wklghd wklghd = this.db().wklghd().get(id);
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(BA05.class.getSimpleName()).
                        name("工作日志").
                        action("删除").
                        message(StringUtil.format("工作日志[{}]删除成功", wklghd.getWkcont())).
                        newValue(wklghd).
                        build();
                this.eventLogManager.critical(event);
                this.db().suplms().delete(id);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("工作日志删除失败", e);
            throw new RestRuntimeException("工作日志已被使用");
        }
    }
}
