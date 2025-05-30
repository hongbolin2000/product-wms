/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.wr02;

import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.StckimPojo;
import com.hongyou.abner.web.UserDataProvider;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.web.ResponseEntry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 库存调整
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/wr02")
public class WR02 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(WR02.class);

    /**
     * 库存调整
     */
    @RequestMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(@RequestBody final StckimPojo pojo) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            Binmas binmas = this.db().binmas().get(pojo.getBinId());
            Stckim stckim = this.db().stckim().get(pojo.getId());
            Stckms stckms = this.db().stckms().get(stckim.getStckid());
            BigDecimal oldQty = stckim.getOnhdqt();

            // 更新库存项
            stckim.binmid(pojo.getBinId()).
                    cartno(pojo.getCartonNo()).
                    onhdqt(pojo.getOnHandQty());
            this.db().stckim().save(stckim);

            // 生成库存日志
            Stcktn stcktn = new Stcktn();
            stcktn.wrhsid(stckim.getWrhsid()).
                    trntyp(Stcktn.TRNTYP.Stock).
                    rdocty(Stcktn.RDOCTY.Other).
                    stimno(stckim.getStimno()).
                    mtrlid(stckms.getMtrlid()).
                    price(stckim.getPrice()).
                    rvodno(stckim.getRvodno()).
                    bathno(stckim.getBathno()).
                    bincde(binmas.getBincde()).
                    cartno(pojo.getCartonNo()).
                    oohdqt(oldQty).
                    nohdqt(stckim.getOnhdqt()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().stcktn().save(stcktn);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("库存调整失败", e);
            throw new RestRuntimeException("库存调整失败");
        }
    }
}
