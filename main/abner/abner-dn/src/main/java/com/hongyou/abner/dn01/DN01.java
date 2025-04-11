/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.dn01;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.SolinePojo;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.DateUtil;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 销售单管理
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/dn01")
public class DN01 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(DN01.class);

    /**
     * 保存销售单
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final SalesOrder pojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Sohead sohead = null; VSohead oldVSohead = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                sohead = this.db().sohead().get(pojo.getId());
                oldVSohead = new VSohead().sohdid(sohead.getSohdid()).oneById();
            }

            // 新增
            if (ObjectUtil.isNull(sohead)) {
                String soHeadNo = DateUtil.format(currentTime, "yyMMddHHmmss");
                sohead = new Sohead();
                sohead.cmpnid(loginUser.getCmpnid()).
                        sohdno(soHeadNo).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            Projms projms = this.db().projms().get(pojo.getProjectId());
            sohead.projid(projms.getProjid()).
                    cstmid(projms.getCstmid()).
                    ordrdt(new Date(currentTime.getTime())).
                    demddt(pojo.getDemandDate()).
                    duedat(pojo.getPlanDueDate()).
                    soctno(pojo.getContractNo()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().sohead().save(sohead);
            VSohead vSohead = new VSohead().sohdid(sohead.getSohdid()).oneById();

            // 记录日志
            String action = oldVSohead == null ? "创建" : "修改";
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "sohead");
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(DN01.class.getSimpleName()).
                    name("销售单管理").
                    action(action).
                    message(StringUtil.format("销售单[{}]{}成功", sohead.getSohdno(), action)).
                    oldValue(oldVSohead).
                    newValue(vSohead).
                    enumsDisplay(displays).
                    build();
            this.eventLogManager.info(event);

            // 已经存在的销售物料id
            Set<Long> solnids = this.db().rpline().listByRPHead(sohead.getSohdid()).stream().
                    map(Rpline::getRplnid).collect(Collectors.toSet());
            for (int i = 0; i < pojo.getMaterials().size(); i++) {
                SolinePojo line = pojo.getMaterials().get(i);

                // 修改
                Soline soline = null; VSoline oldVSoline = null;
                if (ObjectUtil.isNotNull(line.getId())) {
                    soline = this.db().soline().get(line.getId());
                    oldVSoline = new VSoline().solnid(soline.getSolnid()).oneById();
                    solnids.remove(line.getId());
                }

                // 新增
                if (ObjectUtil.isNull(soline)) {
                    soline = new Soline();
                    soline.sohdid(sohead.getSohdid()).
                            cretby(operatorBy).
                            crettm(currentTime);
                }

                soline.solnno(i + 1).
                        mtrlid(line.getMaterialId()).
                        price(line.getPrice()).
                        remark(line.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().soline().save(soline);
                VSoline vSoline = new VSoline().solnid(soline.getSolnid()).oneById();

                // 记录日志
                action = oldVSoline == null ? "创建" : "修改";
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(DN01.class.getSimpleName()).
                        name("销售单管理").
                        action(action + "销售物料").
                        message(StringUtil.format("销售单[{}] 销售物料[{}]{}成功",
                                sohead.getSohdno(), vSoline.getSkunam(), action)
                        ).
                        oldValue(oldVSoline).
                        newValue(vSoline).
                        build();
                this.eventLogManager.info(event);
            }

            // 删除销售物料
            for (Long solnid : solnids) {
                Soline soline = this.db().soline().get(solnid);
                VSoline vSoline = new VSoline().solnid(soline.getSolnid()).oneById();

                // 记录日志
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(DN01.class.getSimpleName()).
                        name("销售单管理").
                        action("删除销售物料").
                        message(StringUtil.format("销售单[{}] 销售物料[{}]删除成功",
                                sohead.getSohdno(), vSoline.getSkunam())
                        ).
                        newValue(vSoline).
                        build();
                this.eventLogManager.info(event);
                this.db().soline().delete(soline);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("销售单保存失败", e);
            throw new RestRuntimeException("销售单保存失败");
        }
    }

    /**
     * 删除销售单
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "sohead");

            for (Long id: ids) {
                Sohead sohead = this.db().sohead().get(id);
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(DN01.class.getSimpleName()).
                        name("销售单管理").
                        action("删除").
                        message(StringUtil.format("销售单[{}]删除成功", sohead.getSohdno())).
                        newValue(sohead).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
                this.db().sohead().delete(sohead);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("销售单删除失败", e);
            throw new RestRuntimeException("销售单已被使用");
        }
    }

    /**
     * 审核销售单
     */
    @PostMapping("/audit")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry audit(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            for (Long id: ids) {
                Sohead sohead = this.db().sohead().get(id);
                sohead.status(Sohead.STATUS.Audited).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().sohead().save(sohead);

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(DN01.class.getSimpleName()).
                        name("销售单管理").
                        action("审核").
                        message(StringUtil.format("销售单[{}]审核通过", sohead.getSohdno())).
                        build();
                this.eventLogManager.info(event);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("销售单审核失败", e);
            throw new RestRuntimeException("销售单审核失败");
        }
    }
}
