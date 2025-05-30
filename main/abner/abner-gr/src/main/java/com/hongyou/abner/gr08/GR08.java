/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr08;

import com.hongyou.abner.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ListUtil;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hongyou.abner.data.model.table.RolineTableDef.ROLINE;

/**
 * 批次/序列号收货
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/gr08")
public class GR08 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(GR08.class);

    /**
     * 批次/序列号收货
     */
    @PostMapping("/receive")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry batchReceive(@RequestBody final ReceiveLinePojo pojo) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            Roline roline = this.db().roline().get(pojo.getId());
            Rohead rohead = this.db().rohead().get(roline.getRohdid());
            Binmas binmas = this.db().binmas().get(pojo.getBinId());

            // 检查序列号
            List<String> serialNos = new ArrayList<>();
            if (StringUtil.isNotBlank(pojo.getSerialNo())) {
                serialNos.addAll(Arrays.stream(pojo.getSerialNo().split("\n")).toList());

                for (String serialNo : serialNos) {
                    Stckim stckim = this.db().stckim().getByStockItemNo(rohead.getWrhsid(), serialNo);
                    if (ObjectUtil.isNotNull(stckim)) {
                        return ResponseEntry.builder().code(-1).message("序列号[ " + serialNo + " ]已存在").build();
                    }
                }
            }

            // 生成批次号
            if (StringUtil.isBlank(roline.getBathno())) {
                String batchNo = this.serialManager.get("stckim.bathno", loginUser.getCmpnid().toString());
                roline.bathno(batchNo).
                        status(Roline.STATUS.Receiving);
            }

            // 生成库存主信息
            Stckms stckms = this.db().stckms().getByMaterial(rohead.getWrhsid(), roline.getMtrlid());
            if (ObjectUtil.isNull(stckms)) {
                stckms = new Stckms();
                stckms.wrhsid(rohead.getWrhsid()).
                        mtrlid(roline.getMtrlid()).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            // 生成库存唯一码
            if (ListUtil.isEmpty(serialNos)) {
                String  stockItemNo = this.serialManager.get("stckim.stimno", loginUser.getCmpnid().toString());
                serialNos.add(stockItemNo);
            }

            for (String serialNo : serialNos) {

                // 更新收货单行数量
                roline.rcvqty(roline.getRcvqty().add(pojo.getReceiveQty())).
                        status(Roline.STATUS.Receiving);

                // 收货单行完成收货
                if (roline.getRcvqty().compareTo(roline.getOrdqty()) >= 0) {
                    roline.status(Roline.STATUS.Finished);
                }
                roline.oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().roline().save(roline);

                // 更新库存主信息
                stckms.onhdqt(stckms.getOnhdqt().add(pojo.getReceiveQty())).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().stckms().save(stckms);

                // 保存收货单项
                Roitem roitem = new Roitem();
                roitem.rolnid(roline.getRolnid()).
                        stimno(serialNo).
                        binmid(binmas.getBinmid()).
                        cartno(pojo.getCartonNo()).
                        rcvqty(pojo.getReceiveQty()).
                        rcvtim(currentTime).
                        recvby(operatorBy).
                        remark(pojo.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().roitem().save(roitem);

                // 生成库存项
                Stckim stckim = new Stckim();
                stckim.stckid(stckms.getStckid()).
                        price(roline.getPrice()).
                        wrhsid(rohead.getWrhsid()).
                        stimno(serialNo).
                        rdocty(rohead.getRdocty()).
                        rcvqty(pojo.getReceiveQty()).
                        rvodno(rohead.getRvodno()).
                        rcvtim(currentTime).
                        rcvdat(new Date(currentTime.getTime())).
                        recvby(operatorBy).
                        suplid(rohead.getSuplid()).
                        projid(rohead.getProjid()).
                        cstmid(rohead.getCstmid()).
                        ownrid(rohead.getOwnrid()).
                        bathno(roline.getBathno()).
                        binmid(pojo.getBinId()).
                        cartno(pojo.getCartonNo()).
                        onhdqt(pojo.getReceiveQty()).
                        remark(pojo.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().stckim().save(stckim);

                // 生成库存日志
                Stcktn stcktn = new Stcktn();
                stcktn.wrhsid(rohead.getWrhsid()).
                        trntyp(Stcktn.TRNTYP.Receive).
                        rdocty(rohead.getRdocty()).
                        stimno(stckim.getStimno()).
                        mtrlid(roline.getMtrlid()).
                        price(roline.getPrice()).
                        rvodno(rohead.getRvodno()).
                        bathno(roline.getBathno()).
                        bincde(binmas.getBincde()).
                        cartno(pojo.getCartonNo()).
                        nohdqt(pojo.getReceiveQty()).
                        remark(pojo.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().stcktn().save(stcktn);
            }

            // 收货单开始收货
            if (StringUtil.isBlank(rohead.getRecvby())) {
                rohead.status(Rohead.STATUS.Receiving).
                        recvby(operatorBy).
                        recvdt(new Date(currentTime.getTime()));
            }

            // 收货单完成收货
            QueryWrapper wrapper = QueryWrapper.create();
            wrapper.and(ROLINE.ROHDID.eq(rohead.getRohdid())).
                    and(ROLINE.STATUS.eq(Roline.STATUS.No).or(ROLINE.STATUS.eq(Roline.STATUS.Receiving)));
            List<Roline> rolines = this.db().roline().list(wrapper);
            if (ListUtil.isEmpty(rolines)) {
                rohead.status(Rohead.STATUS.Finished);
            }
            rohead.oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().rohead().save(rohead);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("收货失败", e);
            throw new RestRuntimeException("收货失败");
        }
    }
}
