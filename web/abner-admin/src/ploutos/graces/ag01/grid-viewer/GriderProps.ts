/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 通用浏览表格界面属性定义
 *
 * @author Berlin
 *******************************************************************************/
import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";

export default class GriderProps {

	/**
	 * 主数据表
	 */
	datatable: Datatable | null = <Datatable>{};

	/**
	 * 子数据表
	 */
	subTables: Datatable[];
}