/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {type VNode} from "vue";
/********************************************************************************
 * 选择列工厂
 *
 * @author Berlin
 *******************************************************************************/
import type ColumnFactory from "@/ploutos/graces/ag01/faces/ColumnFactory.ts";

export default class SelectionColumnFactory implements ColumnFactory {

	/**
	 * 选择列类型
	 */
	static TYPE: string = "selection";

	/**
	 * 获取选择列类型
	 */
	getType(): string {
		return SelectionColumnFactory.TYPE;
	}

	/**
	 * 生成选择列
	 */
	create(): VNode {
		return null;
	}
}