/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr01;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.RqlinePojo;
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
 * 请购单管理
 *
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("/gr01")
public class GR01 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(GR01.class);

    /**
     * 保存请购单
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final RequisitionOrderPojo pojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Rqhead rqhead = null; VRqhead oldVRqhead = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                rqhead = this.db().rqhead().get(pojo.getId());
                oldVRqhead = new VRqhead().rqhdid(rqhead.getRqhdid()).oneById();
            }

            // 新增
            if (ObjectUtil.isNull(rqhead)) {
                String rqHeadNo = DateUtil.format(currentTime, "yyMMddHHmmss");
                rqhead = new Rqhead();
                rqhead.cmpnid(loginUser.getCmpnid()).
                        rqhdno(rqHeadNo).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            Projms projms = this.db().projms().get(pojo.getProjectId());
            rqhead.projid(projms.getProjid()).
                    cstmid(projms.getCstmid()).
                    aplcdt(new Date(currentTime.getTime())).
                    aplcby(operatorBy).
                    demddt(pojo.getDemandDate()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().rqhead().save(rqhead);
            VRqhead vRqhead = new VRqhead().rqhdid(rqhead.getRqhdid()).oneById();

            // 记录日志
            String action = oldVRqhead == null ? "创建" : "修改";
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "rqhead");
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(GR01.class.getSimpleName()).
                    name("请购单管理").
                    action(action).
                    message(StringUtil.format("请购单[{}]{}成功", rqhead.getRqhdno(), action)).
                    oldValue(oldVRqhead).
                    newValue(vRqhead).
                    enumsDisplay(displays).
                    build();
            this.eventLogManager.info(event);

            // 已经存在的请购物料id
            Set<Long> rqlnids = this.db().rpline().listByRPHead(rqhead.getRqhdid()).stream().
                    map(Rpline::getRplnid).collect(Collectors.toSet());
            for (int i = 0; i < pojo.getMaterials().size(); i++) {
                RqlinePojo line = pojo.getMaterials().get(i);

                // 修改
                Rqline rqline = null; VRqline oldVRqline = null;
                if (ObjectUtil.isNotNull(line.getId())) {
                    rqline = this.db().rqline().get(line.getId());
                    oldVRqline = new VRqline().rqlnid(rqline.getRqlnid()).oneById();
                    rqlnids.remove(line.getId());
                }

                // 新增
                if (ObjectUtil.isNull(rqline)) {
                    rqline = new Rqline();
                    rqline.rqhdid(rqhead.getRqhdid()).
                            cretby(operatorBy).
                            crettm(currentTime);
                }

                rqline.rqlnno(i + 1).
                        mtrlid(line.getMaterialId()).
                        ordqty(line.getOrderQty()).
                        price(line.getPrice()).
                        remark(line.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().rqline().save(rqline);
                VRqline vRqline = new VRqline().rqlnid(rqline.getRqlnid()).oneById();

                // 记录日志
                action = oldVRqline == null ? "创建" : "修改";
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR01.class.getSimpleName()).
                        name("请购单管理").
                        action(action + "请购物料").
                        message(StringUtil.format("请购单[{}] 请购物料[{}]{}成功",
                                rqhead.getRqhdno(), vRqline.getSkunam(), action)
                        ).
                        oldValue(oldVRqline).
                        newValue(vRqline).
                        build();
                this.eventLogManager.info(event);
            }

            // 删除请购物料
            for (Long rqlnid : rqlnids) {
                Rqline rqline = this.db().rqline().get(rqlnid);
                VRqline vRqline = new VRqline().rqlnid(rqline.getRqlnid()).oneById();

                // 记录日志
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR01.class.getSimpleName()).
                        name("请购单管理").
                        action("删除请购物料").
                        message(StringUtil.format("请购单[{}] 请购物料[{}]删除成功",
                                rqhead.getRqhdno(), vRqline.getSkunam())
                        ).
                        newValue(vRqline).
                        build();
                this.eventLogManager.info(event);
            }
            this.db().rqline().deleteIds(rqlnids.stream().toList());

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("请购单保存失败", e);
            throw new RestRuntimeException("请购单保存失败");
        }
    }

    /**
     * 删除请购单
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "rqhead");

            for (Long id: ids) {
                VRqhead vRqhead = new VRqhead().rqhdid(id).oneById();
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR01.class.getSimpleName()).
                        name("请购单管理").
                        action("删除").
                        message(StringUtil.format("请购单[{}]删除成功", vRqhead.getRqhdno())).
                        newValue(vRqhead).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
            }
            this.db().rqhead().deleteIds(ids);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("请购单删除失败", e);
            throw new RestRuntimeException("请购单已被使用");
        }
    }

    /**
     * 审核请购单
     */
    @PostMapping("/audit")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry audit(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            for (Long id: ids) {
                Rqhead rqhead = this.db().rqhead().get(id);
                rqhead.status(Rqhead.STATUS.Audited).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().rqhead().save(rqhead);

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR01.class.getSimpleName()).
                        name("请购单管理").
                        action("审核").
                        message(StringUtil.format("请购单[{}]审核通过", rqhead.getRqhdno())).
                        build();
                this.eventLogManager.critical(event);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("请购单审核失败", e);
            throw new RestRuntimeException("请购单审核失败");
        }
    }
}
