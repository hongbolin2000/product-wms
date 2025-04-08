/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr03;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.PolinePojo;
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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 采购单管理
 *
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("/gr03")
public class GR03 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(GR03.class);

    /**
     * 保存采购单
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final PurchasingOrderPojo pojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Pohead pohead = null; VPohead oldVPohead = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                pohead = this.db().pohead().get(pojo.getId());
                oldVPohead = new VPohead().pohdid(pohead.getPohdid()).oneById();
            }

            // 新增
            if (ObjectUtil.isNull(pohead)) {
                String poHeadNo = DateUtil.format(currentTime, "yyMMddHHmmss");
                pohead = new Pohead();
                pohead.cmpnid(loginUser.getCmpnid()).
                        pohdno(poHeadNo).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            if (ObjectUtil.isNotNull(pojo.getProjectId())) {
                Projms projms = this.db().projms().get(pojo.getProjectId());
                pohead.projid(projms.getProjid()).
                        cstmid(projms.getCstmid());
            }

            pohead.suplid(pojo.getSupplierId()).
                    ordrdt(new Date(currentTime.getTime())).
                    demddt(pojo.getDemandDate()).
                    amount(BigDecimal.ZERO).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().pohead().save(pohead);

            // 已经存在的采购物料id
            Set<Long> polnids = this.db().poline().listByPOHead(pohead.getPohdid()).stream().
                    map(Poline::getPolnid).collect(Collectors.toSet());
            for (int i = 0; i < pojo.getMaterials().size(); i++) {
                PolinePojo line = pojo.getMaterials().get(i);

                // 修改
                Poline poline = null; VPoline oldVPoline = null;
                if (ObjectUtil.isNotNull(line.getId())) {
                    poline = this.db().poline().get(line.getId());
                    oldVPoline = new VPoline().polnid(poline.getPolnid()).oneById();
                    polnids.remove(line.getId());
                }

                // 新增
                if (ObjectUtil.isNull(poline)) {
                    poline = new Poline();
                    poline.pohdid(pohead.getPohdid()).
                            cretby(operatorBy).
                            crettm(currentTime);
                }

                poline.polnno(i + 1).
                        mtrlid(line.getMaterialId()).
                        ordqty(line.getOrderQty()).
                        price(line.getPrice()).
                        remark(line.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().pohead().save(pohead);
                VPoline vPoline = new VPoline().polnid(poline.getPolnid()).oneById();

                // 订单总金额
                pohead.amount(pohead.getAmount().add(poline.getOrdqty().multiply(poline.getPrice())));
                this.db().pohead().save(pohead);

                // 记录日志
                String action = oldVPoline == null ? "创建" : "修改";
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR03.class.getSimpleName()).
                        name("采购单管理").
                        action(action + "采购物料").
                        message(StringUtil.format("采购单[{}] 采购物料[{}]{}成功",
                                pohead.getPohdno(), vPoline.getSkunam(), action)
                        ).
                        oldValue(oldVPoline).
                        newValue(vPoline).
                        build();
                this.eventLogManager.info(event);
            }
            VPohead vPohead = new VPohead().pohdid(pohead.getPohdid()).oneById();

            // 记录日志
            String action = oldVPohead == null ? "创建" : "修改";
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "pohead");
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(GR03.class.getSimpleName()).
                    name("采购单管理").
                    action(action).
                    message(StringUtil.format("采购单[{}]{}成功", pohead.getPohdno(), action)).
                    oldValue(oldVPohead).
                    newValue(vPohead).
                    enumsDisplay(displays).
                    build();
            this.eventLogManager.info(event);

            // 删除请购物料
            for (Long polnid : polnids) {
                Poline poline = this.db().poline().get(polnid);
                VPoline vPoline = new VPoline().polnid(poline.getPolnid()).oneById();

                // 记录日志
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR03.class.getSimpleName()).
                        name("采购单管理").
                        action("删除采购物料").
                        message(StringUtil.format("采购单[{}] 采购物料[{}]删除成功",
                                pohead.getPohdno(), vPoline.getSkunam())
                        ).
                        newValue(vPoline).
                        build();
                this.eventLogManager.info(event);
                this.db().poline().delete(poline);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("采购单保存失败", e);
            throw new RestRuntimeException("采购单保存失败");
        }
    }

    /**
     * 删除采购单
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "pohead");

            for (Long id: ids) {
                VPohead vPohead = new VPohead().pohdid(id).oneById();
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR03.class.getSimpleName()).
                        name("请购单管理").
                        action("删除").
                        message(StringUtil.format("请购单[{}]删除成功", vPohead.getPohdno())).
                        newValue(vPohead).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
            }
            this.db().rqhead().deleteIds(ids);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("采购单删除失败", e);
            throw new RestRuntimeException("采购单已被使用");
        }
    }

    /**
     * 审核采购单
     */
    @PostMapping("/audit")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry audit(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            for (Long id: ids) {
                Pohead pohead = this.db().pohead().get(id);
                pohead.status(Pohead.STATUS.Audited).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().pohead().save(pohead);

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR03.class.getSimpleName()).
                        name("采购单管理").
                        action("审核").
                        message(StringUtil.format("采购单[{}]审核通过", pohead.getPohdno())).
                        build();
                this.eventLogManager.critical(event);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("采购单审核失败", e);
            throw new RestRuntimeException("采购单审核失败");
        }
    }
}
