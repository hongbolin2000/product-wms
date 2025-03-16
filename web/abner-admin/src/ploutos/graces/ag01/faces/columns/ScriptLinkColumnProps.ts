/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 脚本列按钮属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";

export default class ScriptLinkColumnProps extends AbstractColumn {

	/**
	 * 执行链接(mode为remote则是服务器地址，为Script则是JavaScript函数)
	 */
	link: string;

	/**
	 * 按钮点击执行模式(script，remote)
	 */
	mode: 'script' | 'remote';

	/**
	 * 确认弹框提示表格字段内容
	 */
	labelColumn: string;

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