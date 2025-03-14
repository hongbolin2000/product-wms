/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * @author Berlin
 *******************************************************************************/
import AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";

/**
 * 路由动作按钮属性
 */
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
	 * 按钮图标
	 */
	icon: string;
}