/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * （浏览表单）浏览表格行属性
 *
 * @author Berlin
 *******************************************************************************/
import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";

export default class DatatableRow {

	/**
	 * 行下的所有浏览表格
	 */
	datatables: Datatable[];

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 非选项卡展示的浏览表格
	 */
	noTabDatatables: Datatable[];

	/**
	 * 选项卡展示的浏览表格
	 */
	tabDatatables: Datatable[];
}