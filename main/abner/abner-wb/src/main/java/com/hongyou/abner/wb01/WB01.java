/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.wb01;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Mtrtyp;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.pojo.MtrtypPojo;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 物料类型管理
 *
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("/wb01")
public class WB01 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(WB01.class);

    /**
     * 保存项目进度
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final MtrtypPojo mtrtypPojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Mtrtyp mtrtyp = null; Mtrtyp oldMtrtyp = null;
            if (ObjectUtil.isNotNull(mtrtypPojo.getId())) {
                mtrtyp = this.db().mtrtyp().get(mtrtypPojo.getId());
                oldMtrtyp = (Mtrtyp) mtrtyp.clone();
            }

            // 新增
            if (ObjectUtil.isNull(mtrtyp)) {
                mtrtyp = new Mtrtyp();
                mtrtyp.cmpnid(loginUser.getCmpnid()).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            // 检查是否已存在
            if (!ObjectUtil.equal(mtrtypPojo.getMaterialType(), mtrtyp.getMttycd())) {
                Mtrtyp existed = this.db().mtrtyp().getByType(loginUser.getCmpnid(), mtrtypPojo.getMaterialType());
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("物料类型已存在").build();
                }
            }

            mtrtyp.mtrctg(mtrtypPojo.getCategory()).
                    mttyp1(mtrtypPojo.getMaterialType1()).
                    mttycd(mtrtypPojo.getMaterialType()).
                    mtrimg(mtrtypPojo.getMaterialImage()).
                    mtsrhd(mtrtypPojo.getHeader()).
                    srilsd(mtrtypPojo.getSeed()).
                    srllen(mtrtypPojo.getLength()).
                    stkmde(mtrtypPojo.getStockMode()).
                    remark(mtrtypPojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().mtrtyp().save(mtrtyp);

            // 记录日志
            String action = oldMtrtyp == null ? "新增" : "修改";
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "mtrtyp");
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(WB01.class.getSimpleName()).
                    name("物料类型管理").
                    action(action).
                    message(StringUtil.format("物料类型[{}]{}成功", mtrtyp.getMttycd(), action)).
                    oldValue(oldMtrtyp).
                    newValue(mtrtyp).
                    enumsDisplay(displays).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("物料类型保存失败", e);
            throw new RestRuntimeException("物料类型保存失败");
        }
    }

    /**
     * 删除物料类型
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "mtrtyp");

            for (Long id: ids) {
                Mtrtyp mtrtyp = this.db().mtrtyp().get(id);
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(WB01.class.getSimpleName()).
                        name("物料类型管理").
                        action("删除").
                        message(StringUtil.format("物料类型[{}]删除成功", mtrtyp.getMttycd())).
                        newValue(mtrtyp).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
                this.db().mtrtyp().delete(mtrtyp);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("物料类型删除失败", e);
            throw new RestRuntimeException("物料类型已被使用");
        }
    }
}
