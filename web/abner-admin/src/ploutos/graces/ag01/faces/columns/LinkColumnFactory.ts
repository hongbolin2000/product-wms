/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 路由列工厂
 *
 * @author Berlin
 *******************************************************************************/
import AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn";
import LinkColumn from "@/ploutos/graces/ag01/faces/columns/LinkColumn.vue";
import type ColumnFactory from "@/ploutos/graces/ag01/faces/ColumnFactory.ts";

export default class LinkColumnFactory implements ColumnFactory {

	/**
	 * 标签列类型
	 */
	static TYPE: string = "link";

	/**
	 * 获取标签列类型
	 */
	getType(): string {
		return LinkColumnFactory.TYPE;
	}

	/**
	 * 生成标签列
	 *
	 * @param column 标签列定义属性
	 */
	create(column: AbstractColumn): VNode {
		return h(LinkColumn, {column: column});
	}
}