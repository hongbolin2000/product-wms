/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.ba02;

import com.hongyou.baron.web.event.EventLog;
import com.hongyou.abner.web.UserDataProvider;
import com.hongyou.abner.data.model.Projms;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.model.VProjms;
import com.hongyou.abner.data.pojo.ProjmsPojo;
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
 * 项目管理
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/ba02")
public class BA02 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(BA02.class);

    /**
     * 保存项目
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final ProjmsPojo pojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();
            String projectCode = pojo.getProjectCode();

            // 修改
            Projms projms = null; VProjms oldVProjms = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                projms = this.db().projms().get(pojo.getId());
                oldVProjms = new VProjms().projid(projms.getProjid()).oneById();
            }

            // 新增
            if (ObjectUtil.isNull(projms)) {
                projms = new Projms();
                projms.cmpnid(loginUser.getCmpnid()).
                        cretby(operatorBy).
                        crettm(currentTime);

                if (StringUtil.isBlank(projectCode)) {
                    projectCode = this.serialManager.get("projms.projcd", loginUser.getCmpnid().toString());
                }
            } else if (StringUtil.isBlank(projectCode)) {
                return ResponseEntry.builder().code(-1).message("项目编号不能为空").build();
            }

            // 检查是否已存在
            if (!ObjectUtil.equal(pojo.getProjectCode(), projms.getProjcd())) {
                Projms existed = this.db().projms().getByCode(loginUser.getCmpnid(), pojo.getProjectCode());
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("项目编号已存在").build();
                }
            }

            projms.cstmid(pojo.getCustomerId()).
                    projcd(projectCode).
                    projnm(pojo.getProjectName()).
                    prjldr(pojo.getProjectLeader()).
                    cntrcd(pojo.getContractCode()).
                    signdt(pojo.getSigningDate()).
                    statdt(pojo.getStartDate()).
                    plfndt(pojo.getPlanFinishDate()).
                    fnshdt(pojo.getFinishedDate()).
                    status(pojo.getStatus()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().projms().save(projms);

            // 记录日志
            VProjms vProjms = new VProjms().projid(projms.getProjid()).oneById();
            String action = oldVProjms == null ? "新增" : "修改";
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "projms");
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(BA02.class.getSimpleName()).
                    name("项目管理").
                    action(action).
                    message(StringUtil.format("项目[{}]{}成功", projms.getProjnm(), action)).
                    oldValue(oldVProjms).
                    newValue(vProjms).
                    enumsDisplay(displays).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("项目保存失败", e);
            throw new RestRuntimeException("项目保存失败");
        }
    }

    /**
     * 删除项目
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "projms");

            for (Long id: ids) {
                VProjms vProjms = new VProjms().projid(id).oneById();

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(BA02.class.getSimpleName()).
                        name("项目管理").
                        action("删除").
                        message(StringUtil.format("项目[{}]删除成功", vProjms.getProjnm())).
                        newValue(vProjms).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
                this.db().projms().delete(id);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("项目删除失败", e);
            throw new RestRuntimeException("项目已被使用");
        }
    }
}
