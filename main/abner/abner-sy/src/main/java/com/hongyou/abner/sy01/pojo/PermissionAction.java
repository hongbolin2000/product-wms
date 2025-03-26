/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy01.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * 菜单权限动作类型定义
 *
 * @author Berlin
 */
@Data
@Builder
public class PermissionAction {

    /**
     * 权限许可编号
     */
    private String permissionCode;

    /**
     * 权限许可名称(菜单显示标签)
     */
    private String permissionName;

    /**
     * 权限动作编号
     */
    private String actionCode;

    /**
     * 权限动作名称
     */
    private String actionName;

    /**
     * 是否分配权限
     */
    private boolean assigned;
}
