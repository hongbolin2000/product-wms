/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
/********************************************************************************
 * 表格列定义
 *
 * @author Berlin
 *******************************************************************************/
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";
import ValueModel from "@/ploutos/graces/ag01/faces/ValueModel";

export default class AbstractColumn extends AbstractComponent {

	/**
	 * 列宽度
	 */
	width: string;

	/**
	 * 过滤器
	 */
	filter: string;

	/**
	 * 过滤选项
	 */
	filterOptions: ValueModel[]

	/**
	 * 当前表格标题
	 */
	datatableTitle: string;

	/**
	 * 操作按钮是否显示在右键选项中
	 */
	option: boolean;

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 当前行数据
	 */
	rowData: any;

	/**
	 * 当前行索引
	 */
	rowIndex: number;
}