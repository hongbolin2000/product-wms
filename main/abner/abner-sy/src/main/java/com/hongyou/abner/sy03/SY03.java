/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy03;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Cmpnms;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.pojo.CmpnmsPojo;
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
import java.util.Map;

/**
 * 公司管理
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/sy03")
public class SY03 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(SY03.class);

    /**
     * 保存公司
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final CmpnmsPojo pojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            Cmpnms cmpnms = this.db().cmpnms().get(pojo.getId());
            Cmpnms oldCmpnms = (Cmpnms) cmpnms.clone();

            // 检查是否已存在
            if (!ObjectUtil.equal(pojo.getCompanyCode(), cmpnms.getCmpncd())) {
                Cmpnms existed = this.db().cmpnms().getByCode(pojo.getCompanyCode());
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("公司编号已存在").build();
                }
            }

            cmpnms.cmpncd(pojo.getCompanyCode()).
                    cmpnnm(pojo.getCompanyName()).
                    cmpnty(pojo.getCompanyType()).
                    cmpmpf(pojo.getCompanyProfile()).
                    indsty(pojo.getIndustry()).
                    ofcwbs(pojo.getOfficialWebsite()).
                    addres(pojo.getAddress()).
                    contcs(pojo.getContacts()).
                    phonno(pojo.getPhoneNo()).
                    email(pojo.getZipCode()).
                    zipcde(pojo.getZipCode()).
                    pftitl(pojo.getPlatformTitle()).
                    pfstil(pojo.getPlatformSubtitle()).
                    pfsptl(pojo.getPlatformSimpleTitle()).
                    capvrf(pojo.getCaptchaVerify()).
                    autlgn(pojo.getAutoLogin()).
                    rmbusn(pojo.getRememberUsername()).
                    rmbpsw(pojo.getRememberPassword()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().cmpnms().save(cmpnms);

            // 记录日志
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "cmpnms");
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(SY03.class.getSimpleName()).
                    name("公司管理").
                    action("修改").
                    message(StringUtil.format("公司[{}]修改成功", cmpnms.getCmpnnm())).
                    oldValue(oldCmpnms).
                    newValue(cmpnms).
                    enumsDisplay(displays).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("公司保存失败", e);
            throw new RestRuntimeException("公司保存失败");
        }
    }
}
