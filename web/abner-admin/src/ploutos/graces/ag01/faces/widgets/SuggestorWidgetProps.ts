/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 查询建议器输入控件属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";

export default class SuggestorWidgetProps extends AbstractWidget {

	/**
	 * 模块号
	 */
	module: string;

	/**
	 * 建议器名称
	 */
	suggestor: string;

	/**
	 * 建议器选择时的脚步
	 */
	script: string;
}