/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.dn04;

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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hongyou.abner.data.model.table.DolineTableDef.DOLINE;
import static com.hongyou.abner.data.model.table.StckimTableDef.STCKIM;

/**
 * 批次/序列号发货
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/dn04")
public class DN04 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(DN04.class);

    /**
     * 批次/序列号发货
     */
    @PostMapping("/deliver")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry batchReceive(@RequestBody final DeliverLinePojo pojo) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            Doline doline = this.db().doline().get(pojo.getId());
            Dohead dohead = this.db().dohead().get(doline.getDohdid());
            Stckms stckms = this.db().stckms().getByMaterial(dohead.getWrhsid(), doline.getMtrlid());

            if (StringUtil.isBlank(pojo.getBinCode()) && StringUtil.isBlank(pojo.getSerialNo())) {
                return ResponseEntry.builder().code(-1).message("请选择序列号").build();
            }
            if (stckms == null) {
                return ResponseEntry.builder().code(-1).message("没有库存").build();
            }
            List<Stckim> stckims = new ArrayList<>();

            // 批次发货
            BigDecimal restDeliverQty = pojo.getDeliverQty();
            if (StringUtil.isNotBlank(pojo.getBinCode())) {

                // 查询库存
                QueryWrapper wrapper = QueryWrapper.create();
                wrapper.and(STCKIM.STCKID.eq(stckms.getStckid())).
                        and(STCKIM.ONHDQT.gt(BigDecimal.ZERO)).
                        orderBy(STCKIM.BATHNO.getName());

                Binmas binmas = this.db().binmas().getByBinCode(dohead.getWrhsid(), pojo.getBinCode());
                wrapper.and(STCKIM.BINMID.eq(binmas.getBinmid()));
                stckims = this.db().stckim().list(wrapper);
            }

            // 序列号发货
            if (StringUtil.isNotBlank(pojo.getSerialNo())) {
                List<String> serialNos = Arrays.stream(pojo.getSerialNo().split("\n")).toList();

                for (String serialNo : serialNos) {
                    Stckim stckim = this.db().stckim().getByStockItemNo(dohead.getWrhsid(), serialNo);
                    if (ObjectUtil.isNull(stckim)) {
                        return ResponseEntry.builder().code(-1).message("未找到序列号[ " + serialNo + " ]库存信息").build();
                    }
                    if (stckim.getOnhdqt().compareTo(BigDecimal.ZERO) <= 0) {
                        return ResponseEntry.builder().code(-1).message("序列号[ " + serialNo + " ]已无库存").build();
                    }
                    if (!stckim.getStckid().equals(stckms.getStckid())) {
                        return ResponseEntry.builder().code(-1).message("序列号[ " + serialNo + " ]不是当前物料的库存").build();
                    }
                    stckims.add(stckim);
                }
            }

            // 检查库存是否充足
            BigDecimal totalStockQty = stckims.stream().map(Stckim::getOnhdqt).
                    reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalStockQty.compareTo(pojo.getDeliverQty()) < 0) {
                return ResponseEntry.builder().code(-1).message("库存数不足").build();
            }

            // 序列号发货
            if (StringUtil.isNotBlank(pojo.getSerialNo())) {
                restDeliverQty = totalStockQty;
            }

            for (Stckim stckim : stckims) {
                BigDecimal deliverQty;
                if (restDeliverQty.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }

                // 在库数大于剩余需发货数
                if (stckim.getOnhdqt().compareTo(restDeliverQty) > 0) {
                    deliverQty = restDeliverQty;
                } else {
                    deliverQty = stckim.getOnhdqt();
                }
                restDeliverQty = restDeliverQty.subtract(deliverQty);

                // 更新发货单行
                doline.dlvqty(doline.getDlvqty().add(deliverQty)).
                        status(Doline.STATUS.Delivering);

                // 保存发货单项
                Doitem doitem = new Doitem();
                doitem.dolnid(doline.getDolnid()).
                        stimno(stckim.getStimno()).
                        binmid(stckim.getBinmid()).
                        bathno(stckim.getBathno()).
                        cartno(stckim.getCartno()).
                        dlvqty(deliverQty).
                        delvtm(currentTime).
                        delvby(operatorBy).
                        remark(pojo.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().doitem().save(doitem);

                // 更新库存主信息
                stckms.onhdqt(stckms.getOnhdqt().subtract(deliverQty)).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().stckms().save(stckms);

                // 更新库存项
                BigDecimal oldOnHandQty = stckim.getOnhdqt();
                stckim.onhdqt(stckim.getOnhdqt().subtract(deliverQty)).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().stckim().save(stckim);

                // 生成库存日志
                VStckim vStckim = new VStckim().stimid(stckim.getStimid()).oneById();
                Stcktn stcktn = new Stcktn();
                stcktn.wrhsid(dohead.getWrhsid()).
                        trntyp(Stcktn.TRNTYP.Deliver).
                        rdocty(dohead.getRdocty()).
                        rdocno(dohead.getRdocno()).
                        stimno(stckim.getStimno()).
                        mtrlid(doline.getMtrlid()).
                        price(doline.getPrice()).
                        dlodno(dohead.getDlodno()).
                        rvodno(stckim.getRvodno()).
                        bathno(stckim.getBathno()).
                        bincde(vStckim.getBincde()).
                        cartno(stckim.getCartno()).
                        oohdqt(oldOnHandQty).
                        nohdqt(stckim.getOnhdqt()).
                        remark(pojo.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().stcktn().save(stcktn);
            }

            // 发货单行完成发货
            if (doline.getDlvqty().compareTo(doline.getOrdqty()) >= 0) {
                doline.status(Doline.STATUS.Finished);
            }
            doline.oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().doline().save(doline);

            // 发货单开始发货
            if (StringUtil.isBlank(dohead.getDelvby())) {
                dohead.status(Dohead.STATUS.Delivering).
                        delvby(operatorBy).
                        delvdt(new Date(currentTime.getTime()));
            }

            // 发货单完成发货
            QueryWrapper wrapper = QueryWrapper.create();
            wrapper.and(DOLINE.DOHDID.eq(dohead.getDohdid())).
                    and(DOLINE.STATUS.eq(Doline.STATUS.No).or(DOLINE.STATUS.eq(Doline.STATUS.Delivering)));
            List<Doline> dolines = this.db().doline().list(wrapper);
            if (ListUtil.isEmpty(dolines)) {
                dohead.status(Dohead.STATUS.Finished);
            }
            dohead.oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().dohead().save(dohead);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("发货失败", e);
            throw new RestRuntimeException("发货失败");
        }
    }
}
