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
import type {DataTableRowKey} from "naive-ui";
import type AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction.ts";

export default class Datatable extends AbstractComponent{

	/**
	 * 动作按钮
	 */
	actions: AbstractAction[];

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
	 * 是否显示边框
	 */
	bordered: boolean;

	/**
	 * 是否显示斑马格
	 */
	striped: boolean;

	/**
	 * 友好提示的列
	 */
	labelColumn: string;

	/**
	 * 表格数据
	 */
	data: [];

	/**
	 * 总记录数
	 */
	total: number;

	/**
	 * 是否选项卡显示（用于浏览表单显示）
	 */
	tab: boolean;

	/**
	 * 是否折叠（用于浏览表单显示）
	 */
	collapse: boolean;

	/**
	 * 表格最大高度（用于浏览表单显示）
	 */
	maxHeight: string;

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 表格选择数据的字段key
	 */
	checkRowKey: string;

	/**
	 * 表格选择的数据
	 */
	checkedKeys: DataTableRowKey[];
}