/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.ba04;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.ProjpcPojo;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 项目进度管理
 *
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("/ba04")
public class BA04 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(BA04.class);

    /**
     * 保存项目进度
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final ProjpcPojo pojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Projpc projpc = null; VProjpc oldVProjpc = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                projpc = this.db().projpc().get(pojo.getId());
                oldVProjpc = new VProjpc().pjpcid(projpc.getProjid()).oneById();
            }

            // 新增
            if (ObjectUtil.isNull(projpc)) {
                projpc = new Projpc();
                projpc.cretby(operatorBy).
                        crettm(currentTime);
            }

            // 检查是否已存在
            if (!ObjectUtil.equal(pojo.getTaskName(), projpc.getTasknm())) {
                Projpc existed = this.db().projpc().getByTaskName(loginUser.getCmpnid(), pojo.getTaskName());
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("任务名称已存在").build();
                }
            }

            projpc.projid(pojo.getProjectId()).
                    level(pojo.getLevel()).
                    tasknm(pojo.getTaskName()).
                    taskrm(pojo.getTaskRemark()).
                    taskld(pojo.getTaskLeader()).
                    pcspct(pojo.getProcessPercent()).
                    status(pojo.getStatus()).
                    plfndt(pojo.getPlanFinishDate()).
                    fnshdt(pojo.getFinishedDate()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().projpc().save(projpc);

            // 记录日志
            VProjpc vProjpc = new VProjpc().pjpcid(projpc.getProjid()).oneById();
            String action = oldVProjpc == null ? "新增" : "修改";
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "projpc");
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(BA04.class.getSimpleName()).
                    name("项目进度管理").
                    action(action).
                    message(StringUtil.format("项目进度[{}]{}成功", projpc.getTasknm(), action)).
                    oldValue(oldVProjpc).
                    newValue(vProjpc).
                    enumsDisplay(displays).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("项目进度保存失败", e);
            throw new RestRuntimeException("项目进度保存失败");
        }
    }

    /**
     * 删除项目进度
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "projpc");

            for (Long id: ids) {
                Projpc projpc = this.db().projpc().get(id);
                VProjpc vProjpc = new VProjpc().pjpcid(projpc.getProjid()).oneById();

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(BA04.class.getSimpleName()).
                        name("项目进度管理").
                        action("删除").
                        message(StringUtil.format("项目进度[{}]删除成功", vProjpc.getTasknm())).
                        newValue(vProjpc).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
                this.db().projms().delete(id);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("项目进度删除失败", e);
            throw new RestRuntimeException("项目进度已被使用");
        }
    }
}
