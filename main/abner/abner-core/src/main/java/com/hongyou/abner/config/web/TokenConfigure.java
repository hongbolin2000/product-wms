/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.web;

import com.hongyou.baron.web.token.TokenConfigureLoader;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Token配置类
 *
 * @author Berlin
 */
@Component
public class TokenConfigure implements TokenConfigureLoader {

    /**
     * 加载不需要登录认证的路径
     */
    @Override
    public List<String> loadExcludePathPatterns() {
        return List.of("/auth/**", "/doc/image", "/doc/download");
    }
}
