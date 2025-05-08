/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.dn05;

import com.hongyou.baron.web.event.EventLog;
import com.hongyou.abner.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.RplinePojo;
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
 * 销售单收款录入
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/dn05")
public class DN05 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(DN05.class);

    /**
     * 收款录入
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(@RequestBody final ReceiptPojo pojo) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            Sohead sohead = this.db().sohead().get(pojo.getId());
            if (Sohead.RCPSTS.Finished.equals(sohead.getRcpsts())) {
                return ResponseEntry.builder().code(-1).message("付款已完成").build();
            }

            Rphead rphead = new Rphead();
            rphead.sohdid(sohead.getSohdid()).
                    rcpamt(pojo.getReceiptAmount()).
                    invcno(pojo.getInvoiceNo()).
                    invcim(pojo.getInvoiceImage()).
                    entrby(operatorBy).
                    entrtm(currentTime).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().rphead().save(rphead);

            List<String> invoiceNos = new ArrayList<>();
            for (RplinePojo line : pojo.getMaterials()) {
                VSoline vSoline = new VSoline().solnid(line.getId()).oneById();

                Rpline rpline = new Rpline();
                rpline.rphdid(rphead.getRphdid()).
                        solnid(vSoline.getSolnid()).
                        rcpamt(line.getReceiptAmount()).
                        invcno(line.getInvoiceNo()).
                        invcim(line.getInvoiceImage()).
                        remark(line.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().rpline().save(rpline);

                invoiceNos.add(rpline.getInvcno());

                // 记录日志
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(DN05.class.getSimpleName()).
                        name("销售单管理").
                        action("收款录入").
                        message(StringUtil.format("销售单[{}] 物料[{}]收款录入成功",
                                sohead.getSohdno(), vSoline.getSkunam())
                        ).
                        newValue(rphead).
                        build();
                this.eventLogManager.info(event);
            }

            // 录入在明细中时
            if (StringUtil.isBlank(rphead.getInvcno())) {
                rphead.invcno(String.join("/", invoiceNos));
                this.db().rphead().save(rphead);
            }

            // 更新采购单
            sohead.rcpcnt(sohead.getRcpcnt() + 1).
                    rcpamt(sohead.getRcpamt().add(rphead.getRcpamt())).
                    oprtby(operatorBy).
                    oprttm(currentTime);

            boolean finished = sohead.getRcpamt().compareTo(sohead.getAmount()) >= 0;
            sohead.rcpsts(finished ? Sohead.RCPSTS.Finished : Sohead.RCPSTS.Part);
            this.db().sohead().save(sohead);

            // 记录日志
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(DN05.class.getSimpleName()).
                    name("销售单管理").
                    action("收款录入").
                    message(StringUtil.format("销售单[{}]收款录入成功", sohead.getSohdno())).
                    newValue(rphead).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("销售单收款录入失败", e);
            throw new RestRuntimeException("销售单收款录入失败");
        }
    }
}
