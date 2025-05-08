/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr05;

import com.hongyou.baron.web.event.EventLog;
import com.hongyou.abner.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.PylinePojo;
import com.hongyou.abner.gr04.GR04;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购单付款录入
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/gr05")
public class GR05 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(GR04.class);

    /**
     * 付款录入
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(@RequestBody final PaymentPojo pojo) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            Pohead pohead = this.db().pohead().get(pojo.getId());
            if (Pohead.PAYSTS.Finished.equals(pohead.getPaysts())) {
                return ResponseEntry.builder().code(-1).message("付款已完成").build();
            }

            Pyhead pyhead = new Pyhead();
            pyhead.pohdid(pohead.getPohdid()).
                    payamt(pojo.getPaymentAmount()).
                    invcno(pojo.getInvoiceNo()).
                    invcim(pojo.getInvoiceImage()).
                    entrby(operatorBy).
                    entrtm(currentTime).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().pyhead().save(pyhead);

            List<String> invoiceNos = new ArrayList<>();
            for (PylinePojo line : pojo.getMaterials()) {
                VPoline vPoline = new VPoline().polnid(line.getId()).oneById();

                Pyline pyline = new Pyline();
                pyline.pyhdid(pyhead.getPyhdid()).
                        polnid(vPoline.getPolnid()).
                        payamt(line.getPaymentAmount()).
                        invcno(line.getInvoiceNo()).
                        invcim(line.getInvoiceImage()).
                        remark(line.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().pyline().save(pyline);
                invoiceNos.add(pyline.getInvcno());

                // 记录日志
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR05.class.getSimpleName()).
                        name("采购单管理").
                        action("付款录入").
                        message(StringUtil.format("采购单[{}] 物料[{}]付款录入成功",
                                pohead.getPohdno(), vPoline.getSkunam())
                        ).
                        newValue(pyhead).
                        build();
                this.eventLogManager.info(event);
            }

            // 录入在明细中时
            if (StringUtil.isBlank(pyhead.getInvcno())) {
                pyhead.invcno(String.join("/", invoiceNos));
                this.db().pyhead().save(pyhead);
            }

            // 更新采购单
            pohead.paycnt(pohead.getPaycnt() + 1).
                    payamt(pohead.getPayamt().add(pyhead.getPayamt())).
                    oprtby(operatorBy).
                    oprttm(currentTime);

            boolean finished = pohead.getPayamt().compareTo(pohead.getAmount()) >= 0;
            pohead.paysts(finished ? Pohead.PAYSTS.Finished : Pohead.PAYSTS.Part);
            this.db().pohead().save(pohead);

            // 记录日志
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(GR05.class.getSimpleName()).
                    name("采购单管理").
                    action("付款录入").
                    message(StringUtil.format("采购单[{}]付款录入成功", pohead.getPohdno())).
                    newValue(pyhead).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("采购单付款录入失败", e);
            throw new RestRuntimeException("采购单付款录入失败");
        }
    }
}
