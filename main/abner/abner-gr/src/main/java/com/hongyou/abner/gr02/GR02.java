/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr02;

import com.hongyou.baron.web.event.EventLog;
import com.hongyou.abner.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.RqlinePojo;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ListUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import com.mybatisflex.core.query.QueryWrapper;
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

import static com.hongyou.abner.data.model.table.RqlineTableDef.RQLINE;

/**
 * 请购单下发采购单
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/gr02")
public class GR02 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(GR02.class);

    /**
     * 下发采购单
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

            Rqhead rqhead = this.db().rqhead().get(pojo.getId());
            String poHeadNo = this.serialManager.get("pohead.pohdno", loginUser.getCmpnid().toString());

            Pohead pohead = new Pohead();
            pohead.cmpnid(loginUser.getCmpnid()).
                    pohdno(poHeadNo).
                    rqhdid(rqhead.getRqhdid()).
                    suplid(pojo.getSupplierId()).
                    projid(rqhead.getProjid()).
                    cstmid(rqhead.getCstmid()).
                    ordrdt(new Date(currentTime.getTime())).
                    demddt(rqhead.getDemddt()).
                    remark(pojo.getRemark()).
                    cretby(operatorBy).
                    crettm(currentTime).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().pohead().save(pohead);

            Map<String, String> polineDisplays = this.international.getTableValuesDisplay(request, "poline");
            Map<String, String> poheadDisplays = this.international.getTableValuesDisplay(request, "pohead");

            BigDecimal amount = BigDecimal.ZERO;
            for (int i = 0; i < pojo.getMaterials().size(); i++) {
                RqlinePojo line = pojo.getMaterials().get(i);
                Rqline rqline = this.db().rqline().get(line.getId());

                Poline poline = new Poline();
                poline.pohdid(pohead.getPohdid()).
                        polnno(i + 1).
                        rqlnid(rqline.getRqlnid()).
                        mtrlid(rqline.getMtrlid()).
                        price(rqline.getPrice()).
                        ordqty(line.getOrderQty()).
                        remark(line.getRemark()).
                        cretby(operatorBy).
                        crettm(currentTime).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().poline().save(poline);
                VPoline vPoline = new VPoline().polnid(poline.getPolnid()).oneById();

                // 订单总金额
                amount = amount.add(poline.getOrdqty().multiply(poline.getPrice()));

                // 更新采购单行
                rqline.status(Rqline.STATUS.Purchase).
                        pcscnt(rqline.getPcscnt() + 1).
                        pcsqty(rqline.getPcsqty().add(line.getOrderQty())).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                if (rqline.getPcsqty().compareTo(rqline.getOrdqty()) >= 0) {
                    rqline.status(Rqline.STATUS.Finish);
                }
                this.db().rqline().save(rqline);

                // 记录日志
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR02.class.getSimpleName()).
                        name("请购单管理").
                        action("下发采购").
                        message(StringUtil.format("请购单[{}] 请购物料[{}]下发采购单[{}]成功",
                                rqhead.getRqhdno(), vPoline.getSkunam(), pohead.getPohdno())
                        ).
                        newValue(vPoline).
                        enumsDisplay(polineDisplays).
                        build();
                this.eventLogManager.info(event);
            }

            // 请购单状态
            QueryWrapper wrapper = QueryWrapper.create();
            wrapper.and(RQLINE.RQHDID.eq(rqhead.getRqhdid())).
                    and(RQLINE.STATUS.eq(Rqline.STATUS.New).or(RQLINE.STATUS.eq(Rqline.STATUS.Purchase)));
            List<Rqline> rqlines = this.db().rqline().list(wrapper);

            // 完成请购
            rqhead.status(ListUtil.isEmpty(rqlines) ? Rqhead.STATUS.Finished : Rqhead.STATUS.Purchase).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().rqhead().save(rqhead);

            // 订单总金额
            pohead.amount(amount);
            this.db().pohead().save(pohead);

            // 记录日志
            VPohead vPohead = new VPohead().pohdid(pohead.getPohdid()).oneById();
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(GR02.class.getSimpleName()).
                    name("请购单管理").
                    action("下发采购").
                    message(StringUtil.format("请购单[{}]下发采购单[{}]成功", rqhead.getRqhdno(), pohead.getPohdno())).
                    newValue(vPohead).
                    enumsDisplay(poheadDisplays).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("请购单下发采购失败", e);
            throw new RestRuntimeException("请购单下发采购失败");
        }
    }
}