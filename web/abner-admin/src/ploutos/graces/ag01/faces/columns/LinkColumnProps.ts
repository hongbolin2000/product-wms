/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 路由动作按钮列属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";

export default class LinkColumnProps extends AbstractColumn {

	/**
	 * 路由地址
	 */
	link: string;

	/**
	 * 按钮执行模式（router，dialog，drawer）
	 */
	mode: 'router' | 'dialog' | 'drawer';

	/**
	 * dialog弹框宽度
	 */
	dialogWidth: string;

	/**
	 * drawer宽度
	 */
	drawerWidth: string;

	/**
	 * 按钮图标
	 */
	icon: string;

	/**
	 * 禁用表达式
	 */
	disabled: string;

	/**
	 * 危险操作
	 */
	danger: boolean;

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 是否禁用
	 */
	isDisabled: boolean;
}