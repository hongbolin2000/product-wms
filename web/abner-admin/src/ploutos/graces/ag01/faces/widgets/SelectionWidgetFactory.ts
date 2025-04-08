/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {type VNode} from "vue";
/********************************************************************************
 * sheeter表格选择列工厂
 *
 * @author Berlin
 *******************************************************************************/
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import type {FormItemRule} from "naive-ui/es/form/src/interface";

export default class SelectionWidgetFactory implements WidgetFactory {

	/**
	 * sheeter表格选择列类型
	 */
	static TYPE: string = "selection";

	/**
	 * 获取类型
	 */
	getType(): string {
		return SelectionWidgetFactory.TYPE;
	}

	/**
	 * 生成sheeter表格选择列
	 */
	create(): VNode {
		return null;
	}

	/**
	 * 控件校验规则
	 */
	getRule(): FormItemRule {
		return {};
	}
}