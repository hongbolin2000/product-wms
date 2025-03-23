/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy01.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 权限菜单树类型定义
 *
 * @author Hong Bo Lin
 */
@Data
@Builder
public class PermissionMenu {

    /**
     * 树节点ID
     */
    private String treeId;

    /**
     * 树节点显示标签
     */
    private String label;

    /**
     * 是否最底层菜单节点
     */
    private boolean isMenu;

    /**
     * 是否权限节点
     */
    private boolean isPermission;

    /**
     * 子节点
     */
    private List<PermissionMenu> children;

    /**
     * 菜单权限动作
     */
    private List<PermissionAction> permissionActions;
}
