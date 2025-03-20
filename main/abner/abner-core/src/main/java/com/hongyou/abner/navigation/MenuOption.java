/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.navigation;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Berlin
 */
@Data
@Builder
public class MenuOption {

    /**
     * 菜单key
     */
    private String key;

    /**
     * 菜单访问路径
     */
    private String path;

    /**
     * 菜单全路径(从父菜单到子菜单拼接)
     */
    private String fullPath;

    /**
     * 菜单标签
     */
    private String label;

    /**
     * 菜单图标
     */
    private String icons;

    /**
     * 父菜单图标
     */
    private String parentIcon;

    /**
     * 是否固定菜单在选项卡中
     */
    private boolean fixed;

    /**
     * 子菜单
     */
    private List<MenuOption> children;
}