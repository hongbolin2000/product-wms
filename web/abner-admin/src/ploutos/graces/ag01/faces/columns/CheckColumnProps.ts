/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 路由动作按钮属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";

export default class CheckColumnProps extends AbstractColumn {

	/**
	 * 是否单选
	 */
	single: boolean;

	/**
	 * 禁用表达式
	 */
	disabled: string;
}