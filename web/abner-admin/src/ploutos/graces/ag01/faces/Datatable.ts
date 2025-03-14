/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 数据表格属性定义
 *
 * @author Berlin
 *******************************************************************************/
import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";
import type DoubleClick from "@/ploutos/graces/ag01/faces/DoubleClick.ts";
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";

export default class Datatable extends AbstractComponent{

	/**
	 * 表格列
	 */
	columns: AbstractColumn[] = [];

	/**
	 * 双击事件
	 */
	doubleClick: DoubleClick;

	/**
	 * 表格宽度
	 */
	width: string;

	/**
	 * 表格数据
	 */
	data: [];

	/**
	 * 总记录数
	 */
	total: number;
}