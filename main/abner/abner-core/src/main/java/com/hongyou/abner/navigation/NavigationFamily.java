/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.navigation;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 导航族
 *
 * @author Hong Bo Lin
 */
@Data
@Builder
public class NavigationFamily {

    /**
     * 导航族名
     */
    private String value;

    /**
     * 导航组标签
     */
    private String label;

    /**
     * 导航组菜单
     */
    private List<MenuOption> menus;
}
