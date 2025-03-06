/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {VNode} from "vue";
/********************************************************************************
 * 表格列工厂定义
 *
 * @author Berlin
 *******************************************************************************/
import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";

export default interface ColumnFactory {

	/**
	 * 表格列类型
	 */
	getType(): string;

	/**
	 * 生成表格列
	 *
	 * @param column 表格列定义属性
	 */
	create(column: AbstractColumn): VNode;
}