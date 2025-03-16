/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 脚本列按钮工厂
 *
 * @author Berlin
 *******************************************************************************/
import type ColumnFactory from "@/ploutos/graces/ag01/faces/ColumnFactory.ts";
import ScriptLinkColumn from "@/ploutos/graces/ag01/faces/columns/ScriptLinkColumn.vue";
import type ScriptLinkColumnProps from "@/ploutos/graces/ag01/faces/columns/ScriptLinkColumnProps.ts";

export default class ScriptLinkColumnFactory implements ColumnFactory {

	/**
	 * 列类型
	 */
	static TYPE: string = "scriptLink";

	/**
	 * 获取列类型
	 */
	getType(): string {
		return ScriptLinkColumnFactory.TYPE;
	}

	/**
	 * 生成列元素
	 *
	 * @param column 列定义属性
	 */
	create(column: ScriptLinkColumnProps): VNode {
		return h(ScriptLinkColumn, {column: column});
	}
}