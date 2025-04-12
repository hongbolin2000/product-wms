/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr04;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.RolinePojo;
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

import static com.hongyou.abner.data.model.table.PolineTableDef.POLINE;

/**
 * 采购单下发收货单
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/gr04")
public class GR04 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(GR04.class);

    /**
     * 下发收货单
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

            Pohead pohead = this.db().pohead().get(pojo.getId());
            String roHeadNo = this.serialManager.get("rohead.rvodno", loginUser.getCmpnid().toString());

            Rohead rohead = new Rohead();
            rohead.wrhsid(pojo.getWarehouseId()).
                    rvodno(roHeadNo).
                    rdocno(pohead.getPohdno()).
                    rdocid(pohead.getPohdid()).
                    rdocty(Rohead.RDOCTY.PurchaseReceive).
                    status(Rohead.STATUS.Audited).
                    suplid(pohead.getSuplid()).
                    projid(pohead.getProjid()).
                    cstmid(pohead.getCstmid()).
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
            this.db().rohead().save(rohead);
            VRohead vRohead = new VRohead().rohdid(rohead.getRohdid()).oneById();

            Map<String, String> rolineDisplays = this.international.getTableValuesDisplay(request, "roline");
            Map<String, String> roheadDisplays = this.international.getTableValuesDisplay(request, "rohead");

            for (int i = 0; i < pojo.getMaterials().size(); i++) {
                RolinePojo line = pojo.getMaterials().get(i);
                Poline poline = this.db().poline().get(line.getId());

                Roline roline = new Roline();
                roline.rohdid(rohead.getRohdid()).
                        rolnno(i + 1).
                        rdocid(poline.getPolnid()).
                        mtrlid(poline.getMtrlid()).
                        price(poline.getPrice()).
                        ordqty(line.getOrderQty()).
                        remark(line.getRemark()).
                        cretby(operatorBy).
                        crettm(currentTime).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().roline().save(roline);
                VRoline vRoline = new VRoline().rolnid(roline.getRolnid()).oneById();

                // 更新采购单行
                poline.status(Poline.STATUS.Receiving).
                        rcvcnt(poline.getRcvcnt() + 1).
                        rcvqty(poline.getRcvqty().add(line.getOrderQty())).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                if (poline.getRcvqty().compareTo(poline.getOrdqty()) >= 0) {
                    poline.status(Poline.STATUS.Finish);
                }
                this.db().poline().save(poline);

                // 记录日志
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR04.class.getSimpleName()).
                        name("采购单管理").
                        action("下发收货").
                        message(StringUtil.format("采购单[{}] 采购物料[{}]下发收货单[{}]成功",
                                pohead.getPohdno(), vRoline.getSkunam(), rohead.getRvodno())
                        ).
                        newValue(vRoline).
                        enumsDisplay(rolineDisplays).
                        build();
                this.eventLogManager.info(event);
            }

            // 开始收货
            if (StringUtil.isBlank(pohead.getStrcby())) {
                pohead.status(Pohead.STATUS.Receiving).
                        strcby(operatorBy).
                        strcdt(new Date(currentTime.getTime()));
            }

            // 采购单状态
            QueryWrapper wrapper = QueryWrapper.create();
            wrapper.and(POLINE.POHDID.eq(pohead.getPohdid())).
                    and(POLINE.STATUS.eq(Poline.STATUS.New).or(POLINE.STATUS.eq(Poline.STATUS.Receiving)));
            List<Poline> polines = this.db().poline().list(wrapper);

            // 完成收货
            if (ListUtil.isEmpty(polines)) {
                pohead.status(Pohead.STATUS.Finished).
                        fnrcby(operatorBy).
                        fnrcdt(new Date(currentTime.getTime()));
            }
            pohead.oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().pohead().save(pohead);

            // 记录日志
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(GR04.class.getSimpleName()).
                    name("采购单管理").
                    action("下发收货").
                    message(StringUtil.format("采购单[{}]下发收货单[{}]成功", pohead.getPohdno(), rohead.getRvodno())).
                    newValue(vRohead).
                    enumsDisplay(roheadDisplays).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("采购单下发收货失败", e);
            throw new RestRuntimeException("采购单下发收货失败");
        }
    }
}