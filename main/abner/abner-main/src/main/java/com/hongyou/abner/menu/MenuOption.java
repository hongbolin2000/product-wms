/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.menu;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Berlin
 */
@Data
@Builder
public class MenuOption {

    /**
     * 菜单路径
     */
    private String key;

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