/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 文本输入控件属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";
import type TextWidgetRefProps from "@/ploutos/graces/ag01/faces/widgets/TextWidgetRefProps.ts";

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

	/**
	 * 输入框的ref(提供给外部页面操作元素)
	 */
	inputRefs: TextWidgetRefProps[];

	/**
	 * 输入框回车事件(提供给外部页面调用)
	 */
	onEnter: (name: string) => void;
}