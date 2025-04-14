/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.dn02;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.DolinePojo;
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

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.hongyou.abner.data.model.table.SolineTableDef.SOLINE;

/**
 * 销售单下发发货单
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/dn02")
public class DN02 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(DN02.class);

    /**
     * 下发发货单
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

            Sohead sohead = this.db().sohead().get(pojo.getId());
            String doHeadNo = this.serialManager.get("dohead.dlodno", loginUser.getCmpnid().toString());

            Dohead dohead = new Dohead();
            dohead.wrhsid(pojo.getWarehouseId()).
                    dlodno(doHeadNo).
                    rdocno(sohead.getSohdno()).
                    rdocid(sohead.getSohdid()).
                    rdocty(Dohead.RDOCTY.SalesDeliver).
                    projid(sohead.getProjid()).
                    cstmid(sohead.getCstmid()).
                    ordrdt(new Date(currentTime.getTime())).
                    carcmp(pojo.getCarrierCompany()).
                    shipno(pojo.getShippingNo()).
                    vhclno(pojo.getVehicleNo()).
                    contcs(pojo.getContacts()).
                    phonno(pojo.getPhoneNo()).
                    remark(pojo.getRemark()).
                    cretby(operatorBy).
                    crettm(currentTime).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().dohead().save(dohead);
            VDohead vDohead = new VDohead().dohdid(dohead.getDohdid()).oneById();

            Map<String, String> dolineDisplays = this.international.getTableValuesDisplay(request, "doline");
            Map<String, String> doheadDisplays = this.international.getTableValuesDisplay(request, "dohead");

            for (int i = 0; i < pojo.getMaterials().size(); i++) {
                DolinePojo line = pojo.getMaterials().get(i);
                Soline soline = this.db().soline().get(line.getId());

                Doline doline = new Doline();
                doline.dohdid(dohead.getDohdid()).
                        dolnno(i + 1).
                        rdocid(soline.getSolnid()).
                        mtrlid(soline.getMtrlid()).
                        price(soline.getPrice()).
                        ordqty(line.getOrderQty()).
                        remark(line.getRemark()).
                        cretby(operatorBy).
                        crettm(currentTime).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().doline().save(doline);
                VDoline vDoline = new VDoline().dolnid(doline.getDolnid()).oneById();

                // 更新销售单行
                soline.status(Soline.STATUS.Delivering).
                        dlvcnt(soline.getDlvcnt() + 1).
                        dlvqty(soline.getDlvqty().add(line.getOrderQty())).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                if (soline.getDlvqty().compareTo(soline.getOrdqty()) >= 0) {
                    soline.status(Soline.STATUS.Finish);
                }
                this.db().soline().save(soline);

                // 记录日志
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(DN02.class.getSimpleName()).
                        name("销售单管理").
                        action("下发发货").
                        message(StringUtil.format("销售单[{}] 销售单[{}]下发发货单[{}]成功",
                                sohead.getSohdno(), vDoline.getSkunam(), dohead.getDlodno())
                        ).
                        newValue(vDoline).
                        enumsDisplay(dolineDisplays).
                        build();
                this.eventLogManager.info(event);
            }

            // 开始发货
            if (StringUtil.isBlank(sohead.getStdlby())) {
                sohead.status(Sohead.STATUS.Delivering).
                        stdlby(operatorBy).
                        stdldt(new Date(currentTime.getTime()));
            }

            // 销购单状态
            QueryWrapper wrapper = QueryWrapper.create();
            wrapper.and(SOLINE.SOHDID.eq(sohead.getSohdid())).
                    and(SOLINE.STATUS.eq(Soline.STATUS.New).or(SOLINE.STATUS.eq(Soline.STATUS.Delivering)));
            List<Soline> solines = this.db().soline().list(wrapper);

            // 完成发货
            if (ListUtil.isEmpty(solines)) {
                sohead.status(Sohead.STATUS.Finished).
                        fndlby(operatorBy).
                        fndldt(new Date(currentTime.getTime()));
            }
            sohead.oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().sohead().save(sohead);

            // 记录日志
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(DN02.class.getSimpleName()).
                    name("销售单管理").
                    action("下发发货").
                    message(StringUtil.format("销售单[{}]下发发货单[{}]成功", sohead.getSohdno(), dohead.getDlodno())).
                    newValue(vDohead).
                    enumsDisplay(doheadDisplays).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("销售单下发发货失败", e);
            throw new RestRuntimeException("销售单下发发货失败");
        }
    }
}