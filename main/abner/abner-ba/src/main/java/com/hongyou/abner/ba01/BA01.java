/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.ba01;

import com.hongyou.baron.web.event.EventLog;
import com.hongyou.abner.web.UserDataProvider;
import com.hongyou.abner.data.model.Splcts;
import com.hongyou.abner.data.model.Suplms;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.pojo.SplctsPojo;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 供应商管理
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/ba01")
public class BA01 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(BA01.class);

    /**
     * 保存供应商
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final SupplierPojo pojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();
            String supplierCode = pojo.getSupplierCode();

            // 修改
            Suplms suplms = null; Suplms oldSuplms = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                suplms = this.db().suplms().get(pojo.getId());
                oldSuplms = (Suplms) suplms.clone();
            }

            // 新增
            if (ObjectUtil.isNull(suplms)) {
                suplms = new Suplms();
                suplms.cmpnid(loginUser.getCmpnid()).
                        cretby(operatorBy).
                        crettm(currentTime);

                if (StringUtil.isBlank(supplierCode)) {
                    supplierCode = this.serialManager.get("suplms.suplcd", loginUser.getCmpnid().toString());
                }
            } else if (StringUtil.isBlank(supplierCode)) {
                return ResponseEntry.builder().code(-1).message("供应商编号不能为空").build();
            }

            // 检查是否已存在
            if (!ObjectUtil.equal(supplierCode, suplms.getSuplcd())) {
                Suplms existed = this.db().suplms().getByCode(loginUser.getCmpnid(), supplierCode);
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("供应商编号已存在").build();
                }
            }

            suplms.suplcd(supplierCode).
                    suplnm(pojo.getSupplierName()).
                    issupl(pojo.getIsSupplier()).
                    iscstm(pojo.getIsCustomer()).
                    addres(pojo.getAddress()).
                    contcs(pojo.getContacts()).
                    phonno(pojo.getPhoneNo()).
                    email(pojo.getEmail()).
                    zipcde(pojo.getZipCode()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().suplms().save(suplms);

            // 记录日志
            String action = oldSuplms == null ? "新增" : "修改";
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "suplms");
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(BA01.class.getSimpleName()).
                    name("供应商管理").
                    action(action).
                    message(StringUtil.format("供应商[{}]{}成功", suplms.getSuplnm(), action)).
                    oldValue(oldSuplms).
                    newValue(suplms).
                    enumsDisplay(displays).
                    build();
            this.eventLogManager.info(event);

            // 已经存在的供应商联系方式id
            Set<Long> spctids = this.db().splcts().listBySupplier(suplms.getSuplid()).stream().
                    map(Splcts::getSpctid).collect(Collectors.toSet());
            for (SplctsPojo line : pojo.getContactLines()) {

                // 修改
                Splcts splcts = null; Splcts oldSplcts = null;
                if (ObjectUtil.isNotNull(line.getId())) {
                    splcts = this.db().splcts().get(line.getId());
                    oldSplcts = (Splcts) splcts.clone();
                    spctids.remove(line.getId());
                }

                // 新增
                if (ObjectUtil.isNull(splcts)) {
                    splcts = new Splcts();
                    splcts.suplid(suplms.getSuplid()).
                            cretby(operatorBy).
                            crettm(currentTime);
                }

                splcts.addres(line.getAddress()).
                        contcs(line.getContacts()).
                        phonno(line.getPhoneNo()).
                        email(line.getEmail()).
                        zipcde(line.getZipCode()).
                        remark(line.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().splcts().save(splcts);

                // 记录日志
                action = oldSplcts == null ? "新增" : "修改";
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(BA01.class.getSimpleName()).
                        name("供应商管理").
                        action(action + "供应商联系方式").
                        message(StringUtil.format("供应商[{}] 联系方式[{}]{}成功",
                                suplms.getSuplnm(), splcts.getAddres(), action)
                        ).
                        oldValue(oldSplcts).
                        newValue(splcts).
                        build();
                this.eventLogManager.info(event);
            }

            // 删除供应商联系方式
            for (Long spctid : spctids) {
                Splcts splcts = this.db().splcts().get(spctid);

                // 记录日志
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(BA01.class.getSimpleName()).
                        name("供应商管理").
                        action("删除供应商联系方式").
                        message(StringUtil.format("供应商[{}] 联系方式[{}]删除成功",
                                suplms.getSuplnm(), splcts.getAddres())
                        ).
                        newValue(splcts).
                        build();
                this.eventLogManager.critical(event);
                this.db().splcts().delete(spctid);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("供应商保存失败", e);
            throw new RestRuntimeException("供应商保存失败");
        }
    }

    /**
     * 删除供应商
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "suplms");

            for (Long id: ids) {
                Suplms suplms = this.db().suplms().get(id);
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(BA01.class.getSimpleName()).
                        name("供应商管理").
                        action("删除").
                        message(StringUtil.format("供应商[{}]删除成功", suplms.getSuplnm())).
                        newValue(suplms).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
                this.db().suplms().delete(id);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("供应商删除失败", e);
            throw new RestRuntimeException("供应商已被使用");
        }
    }
}
