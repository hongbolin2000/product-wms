/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 选择项动作按钮属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction";

export default class CheckLinkActionProps extends AbstractAction {

	/**
	 * 选择项执行模式(script，remote)
	 */
	mode: 'script' | 'remote';

	/**
	 * 执行链接(mode为remote则是服务器地址，为Script则是JavaScript函数)
	 */
	link: string;

	/**
	 * 确认弹框提示表格字段内容
	 */
	labelColumn: string;
}