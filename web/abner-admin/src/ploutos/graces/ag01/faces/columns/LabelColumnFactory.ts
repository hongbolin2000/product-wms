/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {type VNode} from "vue";
/********************************************************************************
 * 标签列工厂
 *
 * @author Berlin
 *******************************************************************************/
import type ColumnFactory from "@/ploutos/graces/ag01/faces/ColumnFactory.ts";

export default class LabelColumnFactory implements ColumnFactory {

	/**
	 * 标签列类型
	 */
	static TYPE: string = "label";

	/**
	 * 获取标签列类型
	 */
	getType(): string {
		return LabelColumnFactory.TYPE;
	}

	/**
	 * 生成标签列
	 */
	create(): VNode {
		return null;
	}
}