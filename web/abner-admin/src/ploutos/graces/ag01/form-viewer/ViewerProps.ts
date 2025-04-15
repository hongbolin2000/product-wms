/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 通用浏览表单属性定义
 *
 * @author Berlin
 *******************************************************************************/
import FormViewerRow from "@/ploutos/graces/ag01/faces/FormViewerRow";
import type DatatableRow from "@/ploutos/graces/ag01/faces/DatatableRow";
import type AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction.ts";

export default class ViewerProps {

	/**
	 * 标题
	 */
	title: string;

	/**
	 * 动作按钮
	 */
	actions: AbstractAction[];

	/**
	 * 浏览表单
	 */
	viewerRows: FormViewerRow[];

	/**
	 * 浏览表格
	 */
	datatableRows: DatatableRow[];
}