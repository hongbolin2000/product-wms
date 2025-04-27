/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
import type {FormItemRule} from "naive-ui/es/form/src/interface";
/********************************************************************************
 * 查询选择器输入控件工厂
 *
 * @author Berlin
 *******************************************************************************/
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import SelectorWidget from "@/ploutos/graces/ag01/faces/widgets/SelectorWidget.vue";
import type SelectorWidgetProps from "@/ploutos/graces/ag01/faces/widgets/SelectorWidgetProps.ts";

export default class SelectorWidgetFactory implements WidgetFactory {

	/**
	 * 控件类型
	 */
	static TYPE: string = "selector";

	/**
	 * 控件类型
	 */
	getType(): string {
		return SelectorWidgetFactory.TYPE;
	}

	/**
	 * 生成控件
	 *
	 * @param widget 控件定义属性
	 */
	create(widget: SelectorWidgetProps): VNode {
		return h(SelectorWidget, {widget: widget});
	}

	/**
	 * 控件校验规则
	 */
	getRule(widget: SelectorWidgetProps): FormItemRule {
		return {
			required: true,
			message: '请选择' + widget.title,
			trigger: ['blur', 'input'],
		}
	}
}