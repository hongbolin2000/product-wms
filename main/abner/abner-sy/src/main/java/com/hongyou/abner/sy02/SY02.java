/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy02;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.pojo.UsermsPojo;
import com.hongyou.abner.util.AesUtil;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 *
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("/sy02")
public class SY02 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(SY02.class);

    /**
     * 保存用户
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final UsermsPojo usermsPojo, @RequestParam final String key,
            final HttpServletRequest request
    ) {

        try {
            Long userCompanyId = this.getUserCompanyId();
            String operatorBy = this.getOperatorBy();
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Userms userms = null; Userms oldUserms = null;
            if (ObjectUtil.isNotNull(usermsPojo.getId())) {
                userms = this.db().userms().get(usermsPojo.getId());
                oldUserms = (Userms) userms.clone();
            }

            // 新增
            if (ObjectUtil.isNull(userms)) {
                String realPassword = AesUtil.ecbDecrypt(key, usermsPojo.getPassword());
                userms = new Userms();
                userms.cmpnid(userCompanyId).
                        paswrd(AesUtil.encrypt(realPassword)).
                        status(Userms.STATUS.Normal).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            // 检查是否已存在
            if (!ObjectUtil.equal(usermsPojo.getUsername(), userms.getUsernm())) {
                Userms existed = this.db().userms().getByUserName(usermsPojo.getUsername());
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("用户名已存在").build();
                }
            }

            userms.usernm(usermsPojo.getUsername()).
                    gender(usermsPojo.getGender()).
                    avatar(usermsPojo.getAvatar()).
                    fulnam(usermsPojo.getFullName()).
                    positn(usermsPojo.getPosition()).
                    phonno(usermsPojo.getPhoneNo()).
                    email(usermsPojo.getEmail()).
                    remark(usermsPojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().userms().save(userms);

            // 记录日志
            String action = oldUserms == null ? "新增" : "修改";
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "userms");
            EventLog event = EventLog.builder().
                    domain(userCompanyId).
                    operator(operatorBy).
                    module(SY02.class.getSimpleName()).
                    name("用户管理").
                    action(action).
                    message(StringUtil.format("用户[{}]{}成功", userms.getUsernm(), action)).
                    oldValue(oldUserms).
                    newValue(userms).
                    enumsDisplay(displays).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("用户保存失败", e);
            throw new RestRuntimeException("用户保存失败");
        }
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Long userCompanyId = this.getUserCompanyId();
            String operatorBy = this.getOperatorBy();

            for (Long id : ids) {
                Userms userms = this.db().userms().get(id);
                Map<String, String> displays = this.international.getTableValuesDisplay(request, "userms");
                EventLog event = EventLog.builder().
                        domain(userCompanyId).
                        operator(operatorBy).
                        module(SY02.class.getSimpleName()).
                        name("用户管理").
                        action("删除").
                        message(StringUtil.format("用户[{}]删除成功", userms.getUsernm())).
                        newValue(userms).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.info(event);
                this.db().userms().delete(userms);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("用户删除失败", e);
            throw new RestRuntimeException("用户已被使用");
        }
    }

    /**
     * 冻结用户
     */
    @PostMapping("/frozen")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry frozen(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Long userCompanyId = this.getUserCompanyId();
            String operatorBy = this.getOperatorBy();
            Timestamp currentTime = this.getCurrentTime();

            for (Long id : ids) {
                Userms userms = this.db().userms().get(id);
                boolean frozen = Userms.STATUS.Frozen.equals(userms.getStatus());
                String action = frozen ? "解除冻结" : "冻结";

                EventLog event = EventLog.builder().
                        domain(userCompanyId).
                        operator(operatorBy).
                        module(SY02.class.getSimpleName()).
                        name("用户管理").
                        action(action).
                        message(StringUtil.format("用户[{}]{}成功", userms.getUsernm(), action)).
                        build();
                this.eventLogManager.info(event);

                userms.status(frozen ? Userms.STATUS.Normal : Userms.STATUS.Frozen).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().userms().save(userms);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("用户删除失败", e);
            throw new RestRuntimeException("用户已被使用");
        }
    }
}
