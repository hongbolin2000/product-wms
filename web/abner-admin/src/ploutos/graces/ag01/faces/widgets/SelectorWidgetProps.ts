/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 查询选择器输入控件属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";

export default class SelectorWidgetProps extends AbstractWidget {

	/**
	 * 模块号
	 */
	module: string;

	/**
	 * 选择器名称
	 */
	selector: string;

	/**
	 * 建议器选择时的脚步
	 */
	script: string;

	/**
	 * 输入参数
	 */
	input: string;

	/**
	 * 显示标签列
	 */
	labelColumn: string;
}