/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 通用浏览界面属性定义
 *
 * @author Berlin
 *******************************************************************************/
import type AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction.ts";
import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";

export default class GriderProps {

	/**
	 * 动作按钮
	 */
	actions: AbstractAction[];

	/**
	 * 主数据表
	 */
	datatable: Datatable = <Datatable>{};

	/**
	 * 子数据表
	 */
	subTables: Datatable[];
}