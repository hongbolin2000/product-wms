/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 表单显示控件属性定义
 *
 * @author Berlin
 *******************************************************************************/
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";

export default class AbstractScene extends AbstractComponent {

	/**
	 * 控件占用列数
	 */
	spans: number;

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 表单值
	 */
	rowData: any;
}