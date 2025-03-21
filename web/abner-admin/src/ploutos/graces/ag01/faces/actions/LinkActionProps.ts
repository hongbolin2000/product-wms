/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 路由动作按钮属性定义
 *
 * @author Berlin
 *******************************************************************************/
import AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction";

/**
 * 路由动作按钮属性
 */
export default class LinkActionProps extends AbstractAction {

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
}