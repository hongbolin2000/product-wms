/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 数字输入控件属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";

export default class NumberWidgetProps extends AbstractWidget {

	/**
	 * 小数精度
	 */
	scale: number;

	/**
	 * 最小值
	 */
	min: number;

	/**
	 * 最大值
	 */
	max: number;

	/**
	 * 前缀
	 */
	prefix: string;

	/**
	 * 后缀
	 */
	suffix: string;

	/**
	 * 建议器选择时的脚步
	 */
	script: string;

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 前缀图标
	 */
	prefixIcon: string;

	/**
	 * 后缀图标
	 */
	suffixIcon: string;
}