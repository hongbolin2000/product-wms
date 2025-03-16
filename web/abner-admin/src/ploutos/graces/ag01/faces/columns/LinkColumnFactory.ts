/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 路由列工厂
 *
 * @author Berlin
 *******************************************************************************/
import LinkColumn from "@/ploutos/graces/ag01/faces/columns/LinkColumn.vue";
import type ColumnFactory from "@/ploutos/graces/ag01/faces/ColumnFactory.ts";
import type LinkColumnProps from "@/ploutos/graces/ag01/faces/columns/LinkColumnProps.ts";

export default class LinkColumnFactory implements ColumnFactory {

	/**
	 * 路由列类型
	 */
	static TYPE: string = "link";

	/**
	 * 获取路由列类型
	 */
	getType(): string {
		return LinkColumnFactory.TYPE;
	}

	/**
	 * 生成路由列
	 *
	 * @param column 路由列定义属性
	 */
	create(column: LinkColumnProps): VNode {
		return h(LinkColumn, {column: column});
	}
}