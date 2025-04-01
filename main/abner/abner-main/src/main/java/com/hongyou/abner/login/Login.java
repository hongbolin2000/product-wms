/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.login;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Cmpnms;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.sy01.SY01;
import com.hongyou.abner.util.AesUtil;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.CaptchaUtil;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.hongyou.abner.data.model.table.CmpnmsTableDef.CMPNMS;

/**
 * 用户登录认证
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/auth")
public class Login extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(Login.class);

    /**
     * 生成图形验证码
     */
    @GetMapping("/captcha")
    public void captcha(final HttpServletResponse response) {
        try {
            CaptchaUtil.createCaptcha(response);
        } catch (Exception e) {
            logger.error("验证码生成失败", e);
        }
    }

    /**
     * 查询平台配置参数
     */
    @GetMapping("/getPlatformConfigure")
    public PlatformConfigure getProperties() {

        try {
            // 如果用户已登录则拿取登录用户所属公司配置否则拿取一个创建公司的配置
            Cmpnms cmpnms; String nikeName = "", avatar = "";
            if (StpUtil.isLogin()) {
                Userms loginUser = this.getLoginUser();
                cmpnms = this.db().cmpnms().get(loginUser.getCmpnid());
                nikeName = StringUtil.blankToDefault(loginUser.getFulnam(), loginUser.getUsernm());
                avatar = loginUser.getAvatar();
            } else {
                QueryWrapper wrapper = QueryWrapper.create();
                wrapper.where("1 = 1 order by " + CMPNMS.CRETTM.getName());
                List<Cmpnms> cmpnmss = this.db().cmpnms().list(wrapper);
                cmpnms = cmpnmss.get(0);
            }

            return PlatformConfigure.builder().
                    companyName(cmpnms.getCmpnnm()).
                    platformTitle(cmpnms.getPftitl()).
                    platformSubtitle(cmpnms.getPfstil()).
                    platformSimpleTitle(cmpnms.getPfsptl()).
                    captchaVerify(Cmpnms.CAPVRF.Open.equals(cmpnms.getCapvrf())).
                    autoLogin(Cmpnms.AUTLGN.Open.equals(cmpnms.getCapvrf())).
                    rememberAccount(Cmpnms.RMBUSN.Open.equals(cmpnms.getCapvrf())).
                    rememberPassword(Cmpnms.RMBPSW.Open.equals(cmpnms.getCapvrf())).
                    nikeName(nikeName).
                    avatar(avatar).
                    build();
        } catch (Exception e) {
            logger.error("查询平台配置参数失败", e);
            throw new RestRuntimeException("查询平台配置参数失败");
        }
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public LoginResult login(@RequestBody final LoginParam param) {

        try {
            // 校验传入参数
            if (StringUtil.isBlank(param.getUsername())) {
                return LoginResult.builder().loginCode(LoginCode.LG001.getValue()).message("用户名不能为空").build();
            }
            if (StringUtil.isBlank(param.getPassword())) {
                return LoginResult.builder().loginCode(LoginCode.LG002.getValue()).message("密码不能为空").build();
            }

            // 检查验证码
            if (StringUtil.isNotBlank(param.getCaptchaValue())) {
                String verify = CaptchaUtil.verify(param.getCaptchaId(), param.getCaptchaValue());
                if (CaptchaUtil.EXPIRE.equals(verify)) {
                    return LoginResult.builder().loginCode(LoginCode.LG002.getValue()).message("验证码已失效").build();
                }
                if (CaptchaUtil.ERROR.equals(verify)) {
                    return LoginResult.builder().loginCode(LoginCode.LG002.getValue()).message("验证码错误").build();
                }
            }

            // 检查用户名是否存在
            Userms userms = this.db().userms().getByUserName(param.getUsername());
            if (ObjectUtil.isNull(userms)) {
                return LoginResult.builder().loginCode(LoginCode.LG003.getValue()).message("用户名或密码不正确").build();
            }

            // 密码解密，对比数据库密码
            String password = AesUtil.ecbDecrypt(param.getKey(), param.getPassword());
            String userPassword = AesUtil.decrypt(userms.getPaswrd());
            if (!password.equals(userPassword)) {
                return LoginResult.builder().loginCode(LoginCode.LG003.getValue()).message("用户名或密码不正确").build();
            }

            // 登录
            this.stpLogin(userms.getUserid(), param.isAutoLogin());
            userms.lgnsts(Userms.LGNSTS.Online).
                    lslgtm(this.getCurrentTime());
            this.db().userms().save(userms);

            // 记录日志
            EventLog event = EventLog.builder().
                    domain(userms.getCmpnid()).
                    operator(this.getOperatorBy(userms)).
                    module(Login.class.getSimpleName()).
                    name("系统").
                    action("用户登录").
                    message(StringUtil.format("用户[{}]登录成功", userms.getUsernm())).
                    build();
            this.eventLogManager.info(event);

            return LoginResult.builder().
                    loginCode(LoginCode.LG200.getValue()).
                    build();
        } catch (Exception e) {
            logger.error("用户登录认证失败", e);
            throw new RestRuntimeException("用户登录认证失败");
        }
    }

    /**
     * STP登录
     */
    private void stpLogin(final Long oprtid, final boolean autoLogin) {
        try {
            StpUtil.checkLogin();
        } catch (NotLoginException e) {
            // 登录时，如果token被冻结，则手动续期，不重新生成新的token
            if (Integer.parseInt(e.getType()) == -6) {
                StpUtil.updateLastActiveToNow();
                return;
            }
            // 7天内自动登录
            if (autoLogin) {
                SaLoginModel model = new SaLoginModel();
                model.setTimeout(60 * 60L * 24 * 7);
                model.setActiveTimeout(-1);
                StpUtil.login(oprtid, model);
            } else {
                StpUtil.login(oprtid);
            }
        }
    }

    /**
     * 检查是否已经登录
     */
    @PostMapping("/isLogin")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public LoginResult isLogin() {
        try {
            StpUtil.checkLogin();

            Userms userms = this.getLoginUser();
            userms.lgnsts(Userms.LGNSTS.Online).
                    lslgtm(this.getCurrentTime());
            this.db().userms().save(userms);
        } catch (NotLoginException e) {
            // token被冻结
            if (Integer.parseInt(e.getType()) == -6) {
                return LoginResult.builder().isLogin(false).isFrozen(true).build();
            } else {
                return LoginResult.builder().isLogin(false).build();
            }
        }
        return LoginResult.builder().isLogin(true).build();
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public String logout() {
        try {
            Userms userms = this.getLoginUser();
            userms.lgnsts(Userms.LGNSTS.Offline);
            this.db().userms().save(userms);
            StpUtil.logout();
            return "Success";
        } catch (Exception e) {
            return "Success";
        }
    }
}
