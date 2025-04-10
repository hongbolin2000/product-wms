/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.wb03;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.model.Wrhsms;
import com.hongyou.abner.data.pojo.WrhsmsPojo;
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
 * 仓库管理
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/wb03")
public class WB03 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(WB03.class);

    /**
     * 保存仓库
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(@RequestBody final WrhsmsPojo pojo) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Wrhsms wrhsms = null; Wrhsms oldWrhsms = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                wrhsms = this.db().wrhsms().get(pojo.getId());
                oldWrhsms = (Wrhsms) wrhsms.clone();
            }

            // 新增
            if (ObjectUtil.isNull(wrhsms)) {
                wrhsms = new Wrhsms();
                wrhsms.cmpnid(loginUser.getCmpnid()).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            // 检查是否已存在
            if (!ObjectUtil.equal(pojo.getWarehouseCode(), wrhsms.getWrhscd())) {
                Wrhsms existed = this.db().wrhsms().getByCode(loginUser.getCmpnid(), pojo.getWarehouseCode());
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("仓库代码已存在").build();
                }
            }

            wrhsms.wrhscd(pojo.getWarehouseCode()).
                    wrhsnm(pojo.getWarehouseName()).
                    apcode(pojo.getAppCode()).
                    addres(pojo.getAddress()).
                    contcs(pojo.getContacts()).
                    phonno(pojo.getPhoneNo()).
                    email(pojo.getEmail()).
                    zipcde(pojo.getZipCode()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().wrhsms().save(wrhsms);

            // 记录日志
            String action = oldWrhsms == null ? "新增" : "修改";
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(WB03.class.getSimpleName()).
                    name("仓库管理").
                    action(action).
                    message(StringUtil.format("仓库[{}]{}成功", wrhsms.getWrhsnm(), action)).
                    oldValue(oldWrhsms).
                    newValue(wrhsms).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("仓库保存失败", e);
            throw new RestRuntimeException("仓库保存失败");
        }
    }

    /**
     * 删除仓库
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);

            for (Long id: ids) {
                Wrhsms wrhsms = this.db().wrhsms().get(id);
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(WB03.class.getSimpleName()).
                        name("仓库管理").
                        action("删除").
                        message(StringUtil.format("仓库[{}]删除成功", wrhsms.getWrhsnm())).
                        newValue(wrhsms).
                        build();
                this.eventLogManager.critical(event);
                this.db().wrhsms().delete(id);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("仓库删除失败", e);
            throw new RestRuntimeException("仓库已被使用");
        }
    }
}
