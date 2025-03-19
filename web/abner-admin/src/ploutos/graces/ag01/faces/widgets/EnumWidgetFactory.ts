/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
import type {FormItemRule} from "naive-ui/es/form/src/interface";
/********************************************************************************
 * 枚举输入控件工厂
 *
 * @author Berlin
 *******************************************************************************/
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import type EnumWidgetProps from "@/ploutos/graces/ag01/faces/widgets/EnumWidgetProps.ts";
import EnumWidget from "@/ploutos/graces/ag01/faces/widgets/EnumWidget.vue";

export default class EnumWidgetFactory implements WidgetFactory {

	/**
	 * 枚举输入控件类型
	 */
	static TYPE: string = "enum";

	/**
	 * 获取枚举输入控件类型
	 */
	getType(): string {
		return EnumWidgetFactory.TYPE;
	}

	/**
	 * 生成枚举输入控件
	 *
	 * @param widget 枚举输入控件定义属性
	 */
	create(widget: EnumWidgetProps): VNode {
		return h(EnumWidget, {widget: widget});
	}

	/**
	 * 枚举输入控件校验规则
	 */
	getRule(widget: EnumWidgetProps): FormItemRule {
		return {
			required: true,
			message: '请选择' + widget.title,
			trigger: ['blur', 'input'],
		}
	}
}