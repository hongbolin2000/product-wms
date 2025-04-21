/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 通用查询选择器界面属性定义
 *
 * @author Berlin
 *******************************************************************************/
import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";

export default class SelectorProps {

	/**
	 * 数据表
	 */
	datatable: Datatable | null = <Datatable>{};
}