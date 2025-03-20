/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy01;

import com.hongyou.abner.data.DataProvider;
import com.hongyou.abner.navigation.NavigationFamily;
import com.hongyou.abner.navigation.NavigationManager;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("sy01")
public class SY01 extends DataProvider {

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
     * 权限分配 - 查询权限列表
     */
    @GetMapping("/getFamilies")
    public List<NavigationFamily> getPermissions() {

        try {
            // 获取到所有导航族
            return this.navigationManager.getFamilies();
        } catch (Exception e) {
            logger.error("查询权限列表异常", e);
            return new ArrayList<>();
        }
    }
}
