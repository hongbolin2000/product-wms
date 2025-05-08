/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.dn03;

import com.hongyou.baron.web.event.EventLog;
import com.hongyou.abner.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.DolinePojo;
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

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 发货单管理
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/dn03")
public class DN03 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(DN03.class);

    /**
     * 保存发货单
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final DeliverOrderPojo pojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Dohead dohead = null; VDohead oldVDohead = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                dohead = this.db().dohead().get(pojo.getId());
                oldVDohead = new VDohead().dohdid(dohead.getDohdid()).oneById();
            }

            // 新增
            if (ObjectUtil.isNull(dohead)) {
                String doHeadNo = this.serialManager.get("dohead.dlodno", loginUser.getCmpnid().toString());
                dohead = new Dohead();
                dohead.dlodno(doHeadNo).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            if (ObjectUtil.isNotNull(pojo.getProjectId())) {
                Projms projms = this.db().projms().get(pojo.getProjectId());
                dohead.projid(projms.getProjid()).
                        cstmid(projms.getCstmid());
            }

            dohead.wrhsid(pojo.getWarehouseId()).
                    rdocno(pojo.getRefDocNo()).
                    rdocty(pojo.getRefDocType()).
                    ordrdt(new Date(currentTime.getTime())).
                    carcmp(pojo.getCarrierCompany()).
                    shipno(pojo.getShippingNo()).
                    vhclno(pojo.getVehicleNo()).
                    contcs(pojo.getContacts()).
                    phonno(pojo.getPhoneNo()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().dohead().save(dohead);
            VDohead vDohead = new VDohead().dohdid(dohead.getDohdid()).oneById();

            Map<String, String> doheadDisplays = this.international.getTableValuesDisplay(request, "dohead");
            Map<String, String> dolineDisplays = this.international.getTableValuesDisplay(request, "doline");

            // 记录日志
            String action = oldVDohead == null ? "创建" : "修改";
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(DN03.class.getSimpleName()).
                    name("发货单管理").
                    action(action).
                    message(StringUtil.format("发货单[{}]{}成功", dohead.getDlodno(), action)).
                    oldValue(oldVDohead).
                    newValue(vDohead).
                    enumsDisplay(doheadDisplays).
                    build();
            this.eventLogManager.info(event);

            // 已经存在的发货物料id
            Set<Long> dolnids = this.db().doline().listByDOHead(dohead.getDohdid()).stream().
                    map(Doline::getDolnid).collect(Collectors.toSet());
            for (int i = 0; i < pojo.getMaterials().size(); i++) {
                DolinePojo line = pojo.getMaterials().get(i);

                // 修改
                Doline doline = null; VDoline oldVDoline = null;
                if (ObjectUtil.isNotNull(line.getId())) {
                    doline = this.db().doline().get(line.getId());
                    oldVDoline = new VDoline().dolnid(doline.getDolnid()).oneById();
                    dolnids.remove(line.getId());
                }

                // 新增
                if (ObjectUtil.isNull(doline)) {
                    doline = new Doline();
                    doline.dohdid(dohead.getDohdid()).
                            cretby(operatorBy).
                            crettm(currentTime);
                }

                doline.dolnno(i + 1).
                        mtrlid(line.getMaterialId()).
                        ordqty(line.getOrderQty()).
                        price(line.getPrice()).
                        remark(line.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().doline().save(doline);
                VDoline vDoline = new VDoline().dolnid(doline.getDolnid()).oneById();

                // 记录日志
                action = oldVDoline == null ? "创建" : "修改";
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(DN03.class.getSimpleName()).
                        name("发货单管理").
                        action(action + "发货物料").
                        message(StringUtil.format("发货单[{}] 发货物料[{}]{}成功",
                                dohead.getDlodno(), vDoline.getSkunam(), action)
                        ).
                        oldValue(oldVDoline).
                        newValue(vDoline).
                        enumsDisplay(dolineDisplays).
                        build();
                this.eventLogManager.info(event);
            }

            // 删除发货物料
            for (Long dolnid : dolnids) {
                VDoline vDoline = new VDoline().dolnid(dolnid).oneById();

                // 记录日志
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(DN03.class.getSimpleName()).
                        name("发货单管理").
                        action("删除发货物料").
                        message(StringUtil.format("发货单[{}] 发货物料[{}]删除成功",
                                dohead.getDlodno(), vDoline.getSkunam())
                        ).
                        newValue(vDoline).
                        enumsDisplay(dolineDisplays).
                        build();
                this.eventLogManager.critical(event);
                this.db().doline().delete(dolnid);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("发购单保存失败", e);
            throw new RestRuntimeException("发购单保存失败");
        }
    }

    /**
     * 删除发货单
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "dohead");

            for (Long id: ids) {
                VDohead vDohead = new VDohead().dohdid(id).oneById();
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(DN03.class.getSimpleName()).
                        name("发货单管理").
                        action("删除").
                        message(StringUtil.format("发货单[{}]删除成功", vDohead.getDlodno())).
                        newValue(vDohead).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
                this.db().rohead().delete(id);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("发货单删除失败", e);
            throw new RestRuntimeException("发货单已被使用");
        }
    }

    /**
     * 审核发货单
     */
    @PostMapping("/audit")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry audit(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            for (Long id: ids) {
                Dohead dohead = this.db().dohead().get(id);
                dohead.status(Dohead.STATUS.Audited).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().dohead().save(dohead);

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(DN03.class.getSimpleName()).
                        name("发货单管理").
                        action("审核").
                        message(StringUtil.format("发货单[{}]审核通过", dohead.getDlodno())).
                        build();
                this.eventLogManager.info(event);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("发货单审核失败", e);
            throw new RestRuntimeException("发货单审核失败");
        }
    }
}
