/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.login;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.hongyou.abner.data.DataProvider;
import com.hongyou.abner.data.model.Oprtms;
import com.hongyou.abner.util.AesUtil;
import com.hongyou.baron.util.CaptchaUtil;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录认证
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/auth")
public class Login extends DataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(Login.class);

    /**
     * 认证配置参数
     */
    private final LoginProperties properties;

    /**
     * 构造函数
     */
    public Login(final LoginProperties properties) {
        this.properties = properties;
    }

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
     * 认证参数
     */
    @GetMapping("/properties")
    public LoginProperties getProperties() {
        return properties;
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
            if (properties.isCaptchaVerify()) {
                String verify = CaptchaUtil.verify(param.getCaptchaId(), param.getCaptchaValue());
                if (CaptchaUtil.EXPIRE.equals(verify)) {
                    return LoginResult.builder().loginCode(LoginCode.LG002.getValue()).message("验证码已失效").build();
                }
                if (CaptchaUtil.ERROR.equals(verify)) {
                    return LoginResult.builder().loginCode(LoginCode.LG002.getValue()).message("验证码错误").build();
                }
            }

            // 检查用户名是否存在
            Oprtms oprtms = this.db().oprtms().getByAccount(param.getUsername());
            if (ObjectUtil.isNull(oprtms)) {
                return LoginResult.builder().loginCode(LoginCode.LG003.getValue()).message("用户名或密码不正确").build();
            }

            // 密码解密，对比数据库密码
            String password = AesUtil.ecbDecrypt(param.getKey(), param.getPassword());
            String userPassword = AesUtil.decrypt(oprtms.getPaswrd());
            if (!password.equals(userPassword)) {
                return LoginResult.builder().loginCode(LoginCode.LG003.getValue()).message("用户名或密码不正确").build();
            }

            // 登录
            this.stpLogin(oprtms.getOprtid(), param.isAutoLogin());
            return LoginResult.builder().
                    loginCode(LoginCode.LG200.getValue()).
                    nikeName(StringUtil.blankToDefault(oprtms.getFulnam(), param.getUsername())).build();
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
    public LoginResult isLogin() {
        try {
            StpUtil.checkLogin();
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
    public String logout() {
        StpUtil.logout();
        return "Success";
    }
}
