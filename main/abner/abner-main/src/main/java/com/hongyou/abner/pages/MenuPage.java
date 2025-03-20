/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.pages;

import com.hongyou.abner.navigation.MenuOption;
import com.hongyou.abner.navigation.NavigationManager;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/menu")
public class MenuPage {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(MenuPage.class);

    /**
     * 导航菜单管理器
     */
    private final NavigationManager navigationManager;

    /**
     * 注入服务
     */
    public MenuPage(final NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
    }

    /**
     * 加载菜单
     *
     * @param familyName 需加载的导航族
     */
    @GetMapping("/load/{familyName}")
    public List<MenuOption> load(@PathVariable final String familyName) {

        try {
            List<MenuOption> menus = this.navigationManager.load(familyName);
            if (ListUtil.isEmpty(menus)) {
                logger.error("未加载到平台菜单: {}", familyName);
                return new ArrayList<>();
            }
            return menus;
        } catch (Exception e) {
            logger.error("菜单加载失败", e);
            return new ArrayList<>();
        }
    }
}
