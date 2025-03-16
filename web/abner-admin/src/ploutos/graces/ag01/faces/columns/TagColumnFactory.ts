/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * tag标签列工厂
 *
 * @author Berlin
 *******************************************************************************/
import type ColumnFactory from "@/ploutos/graces/ag01/faces/ColumnFactory.ts";
import TagColumn from "@/ploutos/graces/ag01/faces/columns/TagColumn.vue";
import type TagColumnProps from "@/ploutos/graces/ag01/faces/columns/TagColumnProps.ts";

export default class TagColumnFactory implements ColumnFactory {

	/**
	 * tag标签列类型
	 */
	static TYPE: string = "tag";

	/**
	 * 获取tag标签列类型
	 */
	getType(): string {
		return TagColumnFactory.TYPE;
	}

	/**
	 * 生成标签列
	 */
	create(column: TagColumnProps): VNode {
		return h(TagColumn, {column: column});
	}
}