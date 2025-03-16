/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 输入控件属性定义
 *
 * @author Berlin
 *******************************************************************************/
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";

export default class AbstractWidget extends AbstractComponent{

	/**
	 * 控件占用列数
	 */
	spans: number;

	/**
	 * 是否必填
	 */
	required: boolean;

	/**
	 * 缺省值
	 */
	default: string;

	/**
	 * 是否作为选项卡标题显示
	 */
	tabtitle: boolean;

	/**
	 * 控件宽度
	 */
	width: string;

	/**
	 * 是否禁用
	 */
	disabled: boolean;

	/**
	 * 表单值
	 */
	rowData: any;
}