/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy01;

import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Pmsnms;
import com.hongyou.baron.web.navigation.Navigate;
import com.hongyou.baron.web.navigation.Family;
import com.hongyou.baron.web.navigation.NavigationManager;
import com.hongyou.abner.sy01.pojo.FamilyOption;
import com.hongyou.abner.sy01.pojo.PermissionAction;
import com.hongyou.abner.sy01.pojo.PermissionMenu;
import com.hongyou.baron.cache.CacheUtil;
import com.hongyou.baron.cache.FIFOCache;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ListUtil;
import com.hongyou.baron.web.ResponseEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色管理
 *
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("/sy01")
public class SY01 extends UserDataProvider {

    /**
     * 缓存权限
     */
    private final FIFOCache<String, Map<String, List<Pmsnms>>> permissionCaches = CacheUtil.newFIFOCache(1);

    /**
     * 缓存权限key
     */
    private final static String PERMISSION_CACHE_KEY = "permissions";

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(SY01.class);

    /**
     * 导航菜单管理器
     */
    private final NavigationManager navigationManager;

    /**
     * 注入服务
     */
    public SY01(final NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
    }

    /**
     * 加载导航族
     */
    @GetMapping("/loadFamilies")
    public ResponseEntry getFamilies() {

        try {
            // 获取到所有导航族选项
            List<Family> families = this.navigationManager.getFamilies();
            List<FamilyOption> familyOptions = new ArrayList<>();
            families.forEach(i -> familyOptions.add(
                    new FamilyOption(i.getFamilyLabel(), i.getFamilyName()))
            );

            return ResponseEntry.builder().body(familyOptions).build();
        } catch (Exception e) {
            logger.error("查询导航族失败", e);
            return ResponseEntry.builder().code(-1).message("查询导航族失败").build();
        }
    }

    /**
     * 加载航族菜单权限
     */
    @GetMapping("/loadPermissions/{family}/{local}")
    public ResponseEntry getPermissions(
            @PathVariable final String family, @PathVariable final String local
    ) {

        try {
            // 获取导航族定义的菜单
            List<Navigate> menus = this.navigationManager.load(family);
            if (menus == null) {
                return ResponseEntry.builder().body(new ArrayList<>()).build();
            }

            // 从缓存中加载系统定义的权限
            Map<String, List<Pmsnms>> permissions;
            if (this.permissionCaches.containsKey(PERMISSION_CACHE_KEY)) {
                permissions = this.permissionCaches.get(PERMISSION_CACHE_KEY);
            } else {
                List<Pmsnms> pmsnmss = this.db().pmsnms().listByCompany(this.getUserCompanyId(), local);
                permissions = pmsnmss.stream().collect(
                        Collectors.groupingBy(Pmsnms::getPmsncd));
                permissionCaches.put(PERMISSION_CACHE_KEY, permissions);
            }

            // 递归加载菜单权限
            List<PermissionMenu> permissionMenus = this.getPermissionMenus(menus, permissions);
            return ResponseEntry.builder().body(permissionMenus).build();
        } catch (Exception e) {
            logger.error("查询导航族菜单权限失败", e);
            return ResponseEntry.builder().code(-1).message("查询导航族菜单权限失败").build();
        }
    }

    /**
     * 递归加载菜单权限
     *
     * @param menus 菜单集合
     */
    private List<PermissionMenu> getPermissionMenus(
            final List<Navigate> menus, final Map<String, List<Pmsnms>> permissions
    ) {
        List<PermissionMenu> permissionMenus = new ArrayList<>();
        for (Navigate menu : menus) {
            PermissionMenu.PermissionMenuBuilder permissionMenu = PermissionMenu.builder();

            // 有子菜单
            if (ListUtil.isNotEmpty(menu.getChildren())) {
                permissionMenu.children(this.getPermissionMenus(menu.getChildren(), permissions));
            }
            List<PermissionAction> permissionActions = new ArrayList<>();

            // 当前菜单权限
            List<Pmsnms> pmsnmss = permissions.get(menu.getId());
            if (ListUtil.isNotEmpty(pmsnmss)) {
                List<PermissionMenu> childrenPermissions = new ArrayList<>();
                pmsnmss.forEach(pmsnms -> {
                    // 当前菜单的权限
                    permissionActions.add(PermissionAction.builder().
                            permissionCode(pmsnms.getPmsncd()).
                            permissionName(menu.getLabel()).
                            actionCode(pmsnms.getActcde()).
                            actionName(pmsnms.getActnam()).
                            assigned(true).
                            build()
                    );

                    // 将权限也作为子菜单
                    childrenPermissions.add(PermissionMenu.builder().
                            treeId(menu.getId() + "@" + pmsnms.getActcde()).
                            label(pmsnms.getActnam()).
                            isPermission(true).build());
                });
                permissionMenu.isMenu(true).
                        children(childrenPermissions);
            }
            permissionMenus.add(permissionMenu.
                    treeId(menu.getId()).
                    label(menu.getLabel()).
                    permissionActions(permissionActions).
                    build());
        }
        return permissionMenus;
    }
}
