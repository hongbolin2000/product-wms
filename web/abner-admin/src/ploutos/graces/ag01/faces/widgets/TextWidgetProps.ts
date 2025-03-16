/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 文本输入控件属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";

export default class TextWidgetProps extends AbstractWidget {

	/**
	 * 最大长度
	 */
	maxLength: number;

	/**
	 * 输入模式(text,textarea,password)
	 */
	mode: 'text' | 'textarea' | 'password';

	/**
	 * 前缀
	 */
	prefix: string;

	/**
	 * 后缀
	 */
	suffix: string;

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