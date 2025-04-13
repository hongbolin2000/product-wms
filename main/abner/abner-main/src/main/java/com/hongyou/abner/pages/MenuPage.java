/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.pages;

import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Pmsnms;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ListUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.navigation.Navigate;
import com.hongyou.baron.web.navigation.NavigationManager;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.hongyou.abner.data.model.table.PmsnmsTableDef.PMSNMS;
import static com.hongyou.abner.data.model.table.RolpmsTableDef.ROLPMS;
import static com.hongyou.abner.data.model.table.UsrrolTableDef.USRROL;

/**
 * 导航菜单
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/menu")
public class MenuPage extends UserDataProvider {

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
    public List<Navigate> load(@PathVariable final String familyName) {

        try {
            List<Navigate> menus = this.navigationManager.load(familyName);
            if (ListUtil.isEmpty(menus)) {
                logger.error("未加载到平台菜单: {}", familyName);
                return new ArrayList<>();
            }

            List<String> permissions = this.loadPermissions();
            return this.loadMenus(menus, permissions);
        } catch (Exception e) {
            logger.error("菜单加载失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 加载有权限的菜单
     */
    private List<Navigate> loadMenus(final List<Navigate> navigates, final List<String> permissions) {
        List<Navigate> menus = new ArrayList<>();
        for (Navigate menu : navigates) {
            if (ListUtil.isNotEmpty(menu.getChildren())) {
                menu.setChildren(this.loadMenus(menu.getChildren(), permissions));
                if (ListUtil.isEmpty(menu.getChildren())) {
                    continue;
                }
            }
            if (ListUtil.isEmpty(menu.getChildren()) && !permissions.contains(menu.getPermission())) {
                continue;
            }
            if (StringUtil.isNotBlank(menu.getAction()) &&
                    !permissions.contains(menu.getPermission() + "@" + menu.getAction())) {
                continue;
            }
            menus.add(menu);
        }
        return menus;
    }

    /**
     * 加载登录用户权限
     */
    private List<String> loadPermissions() {
        Userms loginUser = this.getLoginUser();
        QueryWrapper wrapper = QueryWrapper.create();
        wrapper.select(QueryMethods.distinct(PMSNMS.PMSNCD, PMSNMS.ACTCDE)).
                from(PMSNMS).
                innerJoin(ROLPMS).on(PMSNMS.PMSNID.eq(ROLPMS.PMSNID)).
                innerJoin(USRROL).on(ROLPMS.ROLEID.eq(USRROL.ROLEID)).
                where(USRROL.USERID.eq(loginUser.getUserid()));
        List<Pmsnms> pmsnmss = this.db().pmsnms().list(wrapper);

        List<String> modules = pmsnmss.stream().map(Pmsnms::getPmsncd).toList();
        List<String> moduleActions = pmsnmss.stream().map(i -> i.getPmsncd() + "@" + i.getActcde()).toList();

        List<String> permissions = new ArrayList<>();
        permissions.addAll(modules);
        permissions.addAll(moduleActions);
        return permissions;
    }
}
