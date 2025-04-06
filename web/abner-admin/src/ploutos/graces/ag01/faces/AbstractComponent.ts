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
	 * 控件标题
	 */
	title: string;

	/**
	 * 对其方式
	 */
	align: 'left' | 'center' | 'right'

	/**
	 * 是否隐藏
	 */
	hidden: boolean;

	/**
	 * 忽略字段(用于sheeter中表格需要显示，但是editor中不需要显示的字段)
	 */
	ignore: boolean;
}