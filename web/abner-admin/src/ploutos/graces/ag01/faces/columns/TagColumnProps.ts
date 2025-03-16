/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * tag标签列属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";

export default class TagColumnProps extends AbstractColumn {

	/**
	 * 成功标签展示表达式
	 */
	success: string;

	/**
	 * 警告标签展示表达式
	 */
	warning: string;

	/**
	 * 异常标签展示表达式
	 */
	error: string;

	/**
	 * 标签类型
	 */
	tagType: 'primary' | 'success' | 'warning' | 'error';
}