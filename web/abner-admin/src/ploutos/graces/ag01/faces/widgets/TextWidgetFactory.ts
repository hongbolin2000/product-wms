/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
import type {FormItemRule} from "naive-ui/es/form/src/interface";
/********************************************************************************
 * 文本输入控件工厂
 *
 * @author Berlin
 *******************************************************************************/
import TextWidget from "@/ploutos/graces/ag01/faces/widgets/TextWidget.vue";
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import type TextWidgetProps from "@/ploutos/graces/ag01/faces/widgets/TextWidgetProps.ts";

export default class TextWidgetFactory implements WidgetFactory {

	/**
	 * 文本输入控件类型
	 */
	static TYPE: string = "text";

	/**
	 * 获取文本输入控件类型
	 */
	getType(): string {
		return TextWidgetFactory.TYPE;
	}

	/**
	 * 生成文本输入控件
	 *
	 * @param widget 文本输入控件定义属性
	 */
	create(widget: TextWidgetProps): VNode {
		return h(TextWidget, {widget: widget});
	}

	/**
	 * 文本输入控件校验规则
	 */
	getRule(widget: TextWidgetProps): FormItemRule {
		return {
			required: true,
			message: '请输入' + widget.title,
			trigger: ['blur', 'input'],
		}
	}
}