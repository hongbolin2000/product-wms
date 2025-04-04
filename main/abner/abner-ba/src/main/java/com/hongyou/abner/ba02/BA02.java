/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.ba02;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
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
import com.mybatisflex.core.row.Db;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.hongyou.abner.data.model.table.VProjmsTableDef.VPROJMS;

/**
 * 项目管理
 *
 * @author Hong Bo Lin
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
            @RequestBody final ProjmsPojo projmsPojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Projms projms = null; VProjms oldVProjms = null;
            if (ObjectUtil.isNotNull(projmsPojo.getId())) {
                projms = this.db().projms().get(projmsPojo.getId());

                // 复制试图
                oldVProjms = (VProjms) Db.selectOneById(
                        VPROJMS.getName(), VPROJMS.PROJID.getName(), projms.getProjid()
                ).toEntity(VProjms.class).clone();
            }

            // 新增
            if (ObjectUtil.isNull(projms)) {
                projms = new Projms();
                projms.cmpnid(loginUser.getCmpnid()).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            // 检查是否已存在
            if (!ObjectUtil.equal(projmsPojo.getProjectCode(), projms.getProjcd())) {
                Projms existed = this.db().projms().getByCode(loginUser.getCmpnid(), projmsPojo.getProjectCode());
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("项目编号已存在").build();
                }
            }

            projms.cstmid(projmsPojo.getCustomerId()).
                    projcd(projmsPojo.getProjectCode()).
                    projnm(projmsPojo.getProjectName()).
                    prjldr(projmsPojo.getProjectLeader()).
                    cntrcd(projmsPojo.getContractCode()).
                    signdt(projmsPojo.getSigningDate()).
                    statdt(projmsPojo.getStartDate()).
                    plfndt(projmsPojo.getPlanFinishDate()).
                    fnshdt(projmsPojo.getFinishedDate()).
                    status(projmsPojo.getStatus()).
                    remark(projmsPojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().projms().save(projms);

            // 试图
            VProjms vprojms = Db.selectOneById(
                    VPROJMS.getName(), VPROJMS.PROJID.getName(), projms.getProjid()
            ).toEntity(VProjms.class);

            // 记录日志
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
                    newValue(vprojms).
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
                Projms projms = this.db().projms().get(id);
                VProjms vprojms = Db.selectOneById(
                        VPROJMS.getName(), VPROJMS.PROJID.getName(), projms.getProjid()
                ).toEntity(VProjms.class);

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(BA02.class.getSimpleName()).
                        name("项目管理").
                        action("删除").
                        message(StringUtil.format("项目[{}]删除成功", projms.getProjnm())).
                        newValue(vprojms).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
                this.db().projms().delete(projms);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("项目删除失败", e);
            throw new RestRuntimeException("项目已被使用");
        }
    }
}
