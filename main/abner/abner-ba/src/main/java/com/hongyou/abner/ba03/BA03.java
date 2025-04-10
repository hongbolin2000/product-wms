/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.ba03;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Projdc;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.model.VProjdc;
import com.hongyou.abner.data.pojo.ProjdcPojo;
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

/**
 * 项目文档管理
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/ba03")
public class BA03 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(BA03.class);

    /**
     * 保存项目文档
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(@RequestBody final ProjdcPojo pojo) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Projdc projdc = null; VProjdc oldVProjdc = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                projdc = this.db().projdc().get(pojo.getId());
                oldVProjdc = new VProjdc().pjdcid(projdc.getPjdcid()).oneById();
            }

            // 新增
            if (ObjectUtil.isNull(projdc)) {
                projdc = new Projdc();
                projdc.upldby(operatorBy).
                        upldtm(currentTime);
            }

            // 检查是否已存在
            if (!ObjectUtil.equal(pojo.getDocName(), projdc.getDocnam())) {
                Projdc existed = this.db().projdc().getByDocName(
                        pojo.getProjectId(), pojo.getDocName(), pojo.getDocVersion()
                );
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("文档名称已存在").build();
                }
            }

            projdc.projid(pojo.getProjectId()).
                    docfil(pojo.getDocFile()).
                    docnam(pojo.getDocName()).
                    doctyp(pojo.getDocType()).
                    docvsn(pojo.getDocVersion()).
                    docrmk(pojo.getDocRemark()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().projdc().save(projdc);

            // 记录日志
            VProjdc vProjdc = new VProjdc().pjdcid(projdc.getPjdcid()).oneById();
            String action = oldVProjdc == null ? "新增" : "修改";
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(BA03.class.getSimpleName()).
                    name("项目文档管理").
                    action(action).
                    message(StringUtil.format("项目文档[{}]{}成功", projdc.getDocnam(), action)).
                    oldValue(oldVProjdc).
                    newValue(vProjdc).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("项目文档保存失败", e);
            throw new RestRuntimeException("项目文档保存失败");
        }
    }

    /**
     * 删除项目文档
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);

            for (Long id: ids) {
                VProjdc vProjdc = new VProjdc().pjdcid(id).oneById();

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(BA03.class.getSimpleName()).
                        name("项目文档管理").
                        action("删除").
                        message(StringUtil.format("项目文档[{}]删除成功", vProjdc.getDocnam())).
                        newValue(vProjdc).
                        build();
                this.eventLogManager.critical(event);
                this.db().projdc().delete(id);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("项目文档删除失败", e);
            throw new RestRuntimeException("项目文档已被使用");
        }
    }
}
