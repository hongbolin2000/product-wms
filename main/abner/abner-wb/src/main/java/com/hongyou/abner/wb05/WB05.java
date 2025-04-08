/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.wb05;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Binara;
import com.hongyou.abner.data.model.Binmas;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.model.VBinmas;
import com.hongyou.abner.data.pojo.BinmasPojo;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

/**
 * 库位管理
 *
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("/wb05")
public class WB05 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(WB05.class);

    /**
     * 保存库位
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(@RequestBody final BinmasPojo pojo) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Binmas binmas = null; VBinmas oldVBinmas = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                binmas = this.db().binmas().get(pojo.getId());
                oldVBinmas = new VBinmas().binmid(binmas.getBinmid()).oneById();
            }

            // 新增
            if (ObjectUtil.isNull(binmas)) {
                binmas = new Binmas();
                binmas.cretby(operatorBy).
                        crettm(currentTime);
            }
            Binara binara = this.db().binara().get(pojo.getBinAreaId());

            // 检查是否已存在
            if (!ObjectUtil.equal(pojo.getBinCode(), binmas.getBincde())) {
                Binmas existed = this.db().binmas().getByBinCode(binara.getWrhsid(), pojo.getBinCode());
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("货位编号已存在").build();
                }
            }

            binmas.wrhsid(binara.getWrhsid()).
                    bnarid(pojo.getBinAreaId()).
                    bincde(pojo.getBinCode()).
                    status(pojo.getStatus()).
                    remark(pojo.getRemark()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().binmas().save(binmas);

            // 记录日志
            VBinmas vBinmas = new VBinmas().binmid(binmas.getBinmid()).oneById();
            String action = oldVBinmas == null ? "新增" : "修改";
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(WB05.class.getSimpleName()).
                    name("库位管理").
                    action(action).
                    message(StringUtil.format("库位[{}]{}成功", binmas.getBincde(), action)).
                    oldValue(oldVBinmas).
                    newValue(vBinmas).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("库位保保存失败", e);
            throw new RestRuntimeException("库位保存失败");
        }
    }

    /**
     * 删除库位
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);

            for (Long id: ids) {
                VBinmas vBinmas = new VBinmas().binmid(id);
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(WB05.class.getSimpleName()).
                        name("库位管理").
                        action("删除").
                        message(StringUtil.format("库位[{}]删除成功", vBinmas.getBincde())).
                        newValue(vBinmas).
                        build();
                this.eventLogManager.critical(event);
            }
            this.db().binmas().deleteIds(ids);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("库位删除失败", e);
            throw new RestRuntimeException("库位已被使用");
        }
    }
}
