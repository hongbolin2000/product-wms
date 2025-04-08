/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy02;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.UsermsPojo;
import com.hongyou.abner.util.AesUtil;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.hongyou.abner.data.model.table.UsrrolTableDef.USRROL;
import static com.hongyou.abner.data.model.table.UsrwrhTableDef.USRWRH;

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
            @RequestBody final UsermsPojo pojo, @RequestParam final String key,
            final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Userms userms = null; Userms oldUserms = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                userms = this.db().userms().get(pojo.getId());
                oldUserms = (Userms) userms.clone();
            }

            // 新增
            if (ObjectUtil.isNull(userms)) {
                String realPassword = AesUtil.ecbDecrypt(key, pojo.getPassword());
                userms = new Userms();
                userms.cmpnid(loginUser.getCmpnid()).
                        paswrd(AesUtil.encrypt(realPassword)).
                        status(Userms.STATUS.Normal).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            // 检查是否已存在
            if (!ObjectUtil.equal(pojo.getUsername(), userms.getUsernm())) {
                Userms existed = this.db().userms().getByUserName(pojo.getUsername());
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("用户名已存在").build();
                }
            }

            userms.usernm(pojo.getUsername()).
                    gender(pojo.getGender()).
                    avatar(pojo.getAvatar()).
                    fulnam(pojo.getFullName()).
                    positn(pojo.getPosition()).
                    phonno(pojo.getPhoneNo()).
                    email(pojo.getEmail()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().userms().save(userms);

            // 记录日志
            String action = oldUserms == null ? "新增" : "修改";
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "userms");
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
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
     * 重置用户密码
     */
    @PostMapping("/resetPwd")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry resetPwd(
            @RequestBody final UsermsPojo pojo, @RequestParam final String key
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            Userms userms = this.db().userms().get(pojo.getId());
            Userms oldUserms = (Userms) userms.clone();

            String realPassword = AesUtil.ecbDecrypt(key, pojo.getPassword());
            userms.paswrd(AesUtil.encrypt(realPassword)).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().userms().save(userms);

            // 记录日志
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(SY02.class.getSimpleName()).
                    name("用户管理").
                    action("重置密码").
                    message(StringUtil.format("用户[{}]重置成功", userms.getUsernm())).
                    oldValue(oldUserms).
                    newValue(userms).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("用户密码重置失败", e);
            throw new RestRuntimeException("用户密码重置失败");
        }
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "userms");

            for (Long id: ids) {
                Userms userms = this.db().userms().get(id);
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(SY02.class.getSimpleName()).
                        name("用户管理").
                        action("删除").
                        message(StringUtil.format("用户[{}]删除成功", userms.getUsernm())).
                        newValue(userms).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
            }
            this.db().userms().deleteIds(ids);

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
    public ResponseEntry frozen(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            for (Long id: ids) {
                Userms userms = this.db().userms().get(id);
                boolean frozen = Userms.STATUS.Frozen.equals(userms.getStatus());
                String action = frozen ? "解除冻结" : "冻结";

                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(SY02.class.getSimpleName()).
                        name("用户管理").
                        action(action).
                        message(StringUtil.format("用户[{}]{}成功", userms.getUsernm(), action)).
                        build();
                this.eventLogManager.critical(event);

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

    /**
     * 角色分配
     */
    @PostMapping("/roleAssign")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry roleAssign(@RequestBody final UserRolePojo pojo) {

        try {
            Userms userms = this.db().userms().get(pojo.getId());
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 删除分配的角色
            QueryWrapper wrapper = QueryWrapper.create();
            wrapper.where(USRROL.USERID.eq(userms.getUserid()));
            this.db().usrrol().deleteQuery(wrapper);

            pojo.getRoles().forEach(rolemsPojo -> {
                Rolems rolems = this.db().rolems().get(rolemsPojo.getId());
                Usrrol usrrol = new Usrrol();
                usrrol.userid(userms.getUserid()).
                        roleid(rolems.getRoleid()).
                        remark(rolemsPojo.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().usrrol().save(usrrol);
            });

            // 记录日志
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(SY02.class.getSimpleName()).
                    name("用户管理").
                    action("角色分配").
                    message(StringUtil.format("用户[{}]角色分配成功", userms.getUsernm())).
                    build();
            this.eventLogManager.info(event);
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("角色分配失败", e);
            throw new RestRuntimeException("角色分配失败");
        }
    }

    /**
     * 仓库分配
     */
    @PostMapping("/warehouseAssign")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry warehouseAssign(@RequestBody final UserWarehousePojo pojo) {

        try {
            Userms userms = this.db().userms().get(pojo.getId());
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 删除分配的仓库
            QueryWrapper wrapper = QueryWrapper.create();
            wrapper.where(USRWRH.USERID.eq(userms.getUserid()));
            this.db().usrwrh().deleteQuery(wrapper);

            pojo.getWarehouses().forEach(wrhsmsPojo -> {
                Wrhsms wrhsms = this.db().wrhsms().get(wrhsmsPojo.getId());
                Usrwrh usrwrh = new Usrwrh();
                usrwrh.userid(userms.getUserid()).
                        wrhsid(wrhsms.getWrhsid()).
                        remark(wrhsmsPojo.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().usrwrh().save(usrwrh);
            });

            // 记录日志
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(SY02.class.getSimpleName()).
                    name("用户管理").
                    action("仓库分配").
                    message(StringUtil.format("用户[{}]仓库分配成功", userms.getUsernm())).
                    build();
            this.eventLogManager.info(event);
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("仓库分配失败", e);
            throw new RestRuntimeException("仓库分配失败");
        }
    }
}
