/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 数字输入控件工厂
 *
 * @author Berlin
 *******************************************************************************/
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import NumberWidget from "@/ploutos/graces/ag01/faces/widgets/NumberWidget.vue";
import type NumberWidgetProps from "@/ploutos/graces/ag01/faces/widgets/NumberWidgetProps.ts";

export default class NumberWidgetFactory implements WidgetFactory {

	/**
	 * 数字输入控件类型
	 */
	static TYPE: string = "number";

	/**
	 * 获取数字输入控件类型
	 */
	getType(): string {
		return NumberWidgetFactory.TYPE;
	}

	/**
	 * 生成数字输入控件
	 *
	 * @param widget 数字输入控件定义属性
	 */
	create(widget: NumberWidgetProps): VNode {
		return h(NumberWidget, {widget: widget});
	}
}