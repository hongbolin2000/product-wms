/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.utils;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码工具栏
 *
 * @author Berlin
 */
public class CaptchaUtil {

    private CaptchaUtil() {}

    /**
     * 缓存服务器端生成的验证码
     */
    private static final Cache<String, LineCaptcha> captchaCaches = CacheUtil.newTimedCache(60 * 1000L);

    /**
     * 验证码失效
     */
    public static final String EXPIRE = "Expire";

    /**
     * 验证码错误
     */
    public static final String ERROR = "Error";

    /**
     * 生成验证码
     */
    public static void createCaptcha(final HttpServletResponse response) throws IOException {
        // 生成图形验证码
        LineCaptcha lineCaptcha = cn.hutool.captcha.CaptchaUtil.createLineCaptcha(100, 30, 5, 30);
        lineCaptcha.write(response.getOutputStream());

        // 将验证码存入内存中
        String captchaAgentId = IdUtil.simpleUUID();
        captchaCaches.put(captchaAgentId, lineCaptcha);

        // 将验证码ID放入headers中
        response.setContentType("image/jpeg");
        response.addHeader("Captcha-Id", captchaAgentId);
        response.addHeader("Access-Control-Expose-Headers", "Captcha-Id");
    }

    /**
     * 检验验证码
     */
    public static String verify(final String captchaId, final String captchaValue) {
        LineCaptcha lineCaptcha = captchaCaches.get(captchaId, false);
        if (ObjectUtil.isNull(lineCaptcha)) {
            return EXPIRE;
        }
        boolean verified = lineCaptcha.verify(captchaValue);
        if (!verified) {
            return ERROR;
        }
        captchaCaches.remove(captchaId);
        return "Success";
    }
}
