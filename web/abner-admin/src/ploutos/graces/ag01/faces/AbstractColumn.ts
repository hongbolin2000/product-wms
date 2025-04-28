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
	width: number;

	/**
	 * 过滤器
	 */
	filter: string;

	/**
	 * 过滤选项
	 */
	filterOptions: ValueModel[]

	/**
	 * 操作按钮是否显示在右键选项中
	 */
	option: boolean;

	/**
	 * 是否总结栏
	 */
	summary: boolean;

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 模块号
	 */
	module: string;

	/**
	 * 通用界面名称
	 */
	moduleName: string;

	/**
	 * 当前表格标题
	 */
	datatableTitle: string;

	/**
	 * 当前行数据
	 */
	rowData: any;

	/**
	 * 当前行索引
	 */
	rowIndex: number;
}