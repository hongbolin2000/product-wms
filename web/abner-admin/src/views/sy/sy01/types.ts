/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 角色管理属性类型定义
 *
 * @author Berlin
 *******************************************************************************/

/**
 * 菜单权限树类型定义
 */
export type PermissionMenu = {

	/**
	 * 树节点ID
	 */
	treeId: string;

	/**
	 * 树节点显示标签
	 */
	label: string;

	/**
	 * 是否最底层菜单节点
	 */
	menu: string;

	/**
	 * 是否权限节点
	 */
	permission: boolean;

	/**
	 * 子节点
	 */
	children: PermissionMenu[];

	/**
	 * 菜单权限动作
	 */
	permissionActions: PermissionAction[];
}

/**
 * 菜单权限动作类型定义
 */
export type PermissionAction = {

	/**
	 * 权限许可编号
	 */
	permissionCode: string;

	/**
	 * 权限许可名称(菜单显示标签)
	 */
	permissionName: string;

	/**
	 * 权限动作编号
	 */
	actionCode: string;

	/**
	 * 权限动作名称
	 */
	actionName: string;

	/**
	 * 是否已分配
	 */
	assigned: boolean;
}