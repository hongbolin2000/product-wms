/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 控件定义，组件的父类，描述共同的特点
 *
 * @author Berlin
 *******************************************************************************/
export default class AbstractComponent {

	/**
	 * 控件类型
	 */
	type: string;

	/**
	 * 控件名称
	 */
	name: string;

	/**
	 * 是否隐藏
	 */
	hidden: boolean;
}