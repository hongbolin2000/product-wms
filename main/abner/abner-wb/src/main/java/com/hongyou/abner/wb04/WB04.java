/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.wb04;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Binara;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.model.VBinara;
import com.hongyou.abner.data.pojo.BinaraPojo;
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
 * 库区管理
 *
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("/wb04")
public class WB04 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(WB04.class);

    /**
     * 保存库区
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(@RequestBody final BinaraPojo pojo) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Binara binara = null; VBinara oldVBinara = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                binara = this.db().binara().get(pojo.getId());
                oldVBinara = new VBinara().bnarid(binara.getBnarid()).oneById();
            }

            // 新增
            if (ObjectUtil.isNull(binara)) {
                binara = new Binara();
                binara.cretby(operatorBy).
                        crettm(currentTime);
            }

            // 检查是否已存在
            if (!ObjectUtil.equal(pojo.getAreaCode(), binara.getBnarcd())) {
                Binara existed = this.db().binara().getByCode(loginUser.getCmpnid(), pojo.getAreaCode());
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("库区代码已存在").build();
                }
            }

            binara.wrhsid(pojo.getWarehouseId()).
                    bnarcd(pojo.getAreaCode()).
                    bnarnm(pojo.getAreaName()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().binara().save(binara);

            // 记录日志
            VBinara vBinara = new VBinara().bnarid(binara.getBnarid()).oneById();
            String action = oldVBinara == null ? "新增" : "修改";
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(WB04.class.getSimpleName()).
                    name("库区管理").
                    action(action).
                    message(StringUtil.format("库区[{}]{}成功", binara.getBnarnm(), action)).
                    oldValue(oldVBinara).
                    newValue(vBinara).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("库区保存失败", e);
            throw new RestRuntimeException("库区保存失败");
        }
    }

    /**
     * 删除库区
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);

            for (Long id: ids) {
                VBinara vBinara = new VBinara().bnarid(id).oneById();
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(WB04.class.getSimpleName()).
                        name("库区管理").
                        action("删除").
                        message(StringUtil.format("库区[{}]删除成功", vBinara.getBnarnm())).
                        newValue(vBinara).
                        build();
                this.eventLogManager.critical(event);
                this.db().binara().delete(id);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("库区删除失败", e);
            throw new RestRuntimeException("库区已被使用");
        }
    }
}
