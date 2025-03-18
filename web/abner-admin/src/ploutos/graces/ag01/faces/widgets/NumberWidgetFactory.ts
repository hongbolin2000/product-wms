/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
import type {FormItemRule} from "naive-ui/es/form/src/interface";
/********************************************************************************
 * 数字输入控件工厂
 *
 * @author Berlin
 *******************************************************************************/
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import NumberWidget from "@/ploutos/graces/ag01/faces/widgets/NumberWidget.vue";
import type NumberWidgetProps from "@/ploutos/graces/ag01/faces/widgets/NumberWidgetProps.ts";
import CheckWidgetProps from "@/ploutos/graces/ag01/faces/widgets/CheckWidgetProps.ts";

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

	/**
	 * 数字输入控件校验规则
	 */
	getRule(widget: CheckWidgetProps): FormItemRule {
		return {
			required: true,
			type: 'number',
			message: '请输入' + widget.title,
			trigger: ['blur', 'input'],
		}
	}
}