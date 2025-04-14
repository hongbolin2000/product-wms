/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr07;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.RolinePojo;
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
 * 收货单管理
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/gr07")
public class GR07 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(GR07.class);

    /**
     * 保存收货单
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final ReceiveOrderPojo pojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Rohead rohead = null; VRohead oldVRohead = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                rohead = this.db().rohead().get(pojo.getId());
                oldVRohead = new VRohead().rohdid(rohead.getRohdid()).oneById();
            }

            // 新增
            if (ObjectUtil.isNull(rohead)) {
                String roHeadNo = this.serialManager.get("rohead.rvodno", loginUser.getCmpnid().toString());
                rohead = new Rohead();
                rohead.rvodno(roHeadNo).
                        status(Rohead.STATUS.Audited).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            if (ObjectUtil.isNotNull(pojo.getProjectId())) {
                Projms projms = this.db().projms().get(pojo.getProjectId());
                rohead.projid(projms.getProjid()).
                        cstmid(projms.getCstmid());
            }

            rohead.wrhsid(pojo.getWarehouseId()).
                    rdocno(pojo.getRefDocNo()).
                    rdocty(pojo.getRefDocType()).
                    suplid(pojo.getSupplierId()).
                    ordrdt(new Date(currentTime.getTime())).
                    carcmp(pojo.getCarrierCompany()).
                    shipno(pojo.getShippingNo()).
                    vhclno(pojo.getVehicleNo()).
                    contcs(pojo.getContacts()).
                    phonno(pojo.getPhoneNo()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().rohead().save(rohead);
            VRohead vRohead = new VRohead().rohdid(rohead.getRohdid()).oneById();

            Map<String, String> roheadDisplays = this.international.getTableValuesDisplay(request, "rohead");
            Map<String, String> rolineDisplays = this.international.getTableValuesDisplay(request, "roline");

            // 记录日志
            String action = oldVRohead == null ? "创建" : "修改";
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(GR07.class.getSimpleName()).
                    name("收货单管理").
                    action(action).
                    message(StringUtil.format("收货单[{}]{}成功", rohead.getRvodno(), action)).
                    oldValue(oldVRohead).
                    newValue(vRohead).
                    enumsDisplay(roheadDisplays).
                    build();
            this.eventLogManager.info(event);

            // 已经存在的收货物料id
            Set<Long> rolnids = this.db().roline().listByROHead(rohead.getRohdid()).stream().
                    map(Roline::getRolnid).collect(Collectors.toSet());
            for (int i = 0; i < pojo.getMaterials().size(); i++) {
                RolinePojo line = pojo.getMaterials().get(i);

                // 修改
                Roline roline = null; VRoline oldVRoline = null;
                if (ObjectUtil.isNotNull(line.getId())) {
                    roline = this.db().roline().get(line.getId());
                    oldVRoline = new VRoline().rolnid(roline.getRolnid()).oneById();
                    rolnids.remove(line.getId());
                }

                // 新增
                if (ObjectUtil.isNull(roline)) {
                    roline = new Roline();
                    roline.rohdid(rohead.getRohdid()).
                            cretby(operatorBy).
                            crettm(currentTime);
                }

                roline.rolnno(i + 1).
                        mtrlid(line.getMaterialId()).
                        ordqty(line.getOrderQty()).
                        price(line.getPrice()).
                        remark(line.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().roline().save(roline);
                VRoline vRoline = new VRoline().rolnid(roline.getRolnid()).oneById();

                // 记录日志
                action = oldVRoline == null ? "创建" : "修改";
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR07.class.getSimpleName()).
                        name("收货单管理").
                        action(action + "收货物料").
                        message(StringUtil.format("收货单[{}] 收货物料[{}]{}成功",
                                rohead.getRvodno(), vRoline.getSkunam(), action)
                        ).
                        oldValue(oldVRoline).
                        newValue(vRoline).
                        enumsDisplay(rolineDisplays).
                        build();
                this.eventLogManager.info(event);
            }

            // 删除收货物料
            for (Long rolnid : rolnids) {
                VRoline vRoline = new VRoline().rolnid(rolnid).oneById();

                // 记录日志
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR07.class.getSimpleName()).
                        name("收货单管理").
                        action("删除收货物料").
                        message(StringUtil.format("收货单[{}] 收货物料[{}]删除成功",
                                rohead.getRvodno(), vRoline.getSkunam())
                        ).
                        newValue(vRoline).
                        enumsDisplay(rolineDisplays).
                        build();
                this.eventLogManager.critical(event);
                this.db().rqline().delete(rolnid);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("收货单保存失败", e);
            throw new RestRuntimeException("收货单保存失败");
        }
    }

    /**
     * 删除收货单
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "rohead");

            for (Long id: ids) {
                VRohead vRohead = new VRohead().rohdid(id).oneById();
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR07.class.getSimpleName()).
                        name("收货单管理").
                        action("删除").
                        message(StringUtil.format("收货单[{}]删除成功", vRohead.getRvodno())).
                        newValue(vRohead).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
                this.db().rohead().delete(id);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("收货单删除失败", e);
            throw new RestRuntimeException("收货单已被使用");
        }
    }

    /**
     * 审核收货单
     */
    @PostMapping("/audit")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry audit(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            for (Long id: ids) {
                Rohead rohead = this.db().rohead().get(id);
                rohead.status(Rohead.STATUS.Audited).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().rohead().save(rohead);

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR07.class.getSimpleName()).
                        name("收货单管理").
                        action("审核").
                        message(StringUtil.format("收货单[{}]审核通过", rohead.getRvodno())).
                        build();
                this.eventLogManager.info(event);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("收货单审核失败", e);
            throw new RestRuntimeException("收货单审核失败");
        }
    }
}
