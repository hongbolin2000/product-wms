/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
/********************************************************************************
 * 表格列定义
 *
 * @author Berlin
 *******************************************************************************/
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";

export default class AbstractColumn extends AbstractComponent{

	/**
	 * 列标题
	 */
	title: string;

	/**
	 * 列宽度
	 */
	width: number;

	/**
	 * 当前行数据
	 */
	rowData: any;

	/**
	 * 当前行索引
	 */
	rowIndex: number;
}