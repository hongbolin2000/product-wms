/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
import type {FormItemRule} from "naive-ui/es/form/src/interface";
/********************************************************************************
 * 查询建议器输入控件工厂
 *
 * @author Berlin
 *******************************************************************************/
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import SuggestorWidget from "@/ploutos/graces/ag01/faces/widgets/SuggestorWIdget.vue";
import type SuggestorWidgetProps from "@/ploutos/graces/ag01/faces/widgets/SuggestorWidgetProps.ts";
import CheckWidgetProps from "@/ploutos/graces/ag01/faces/widgets/CheckWidgetProps.ts";

export default class SuggestorWidgetFactory implements WidgetFactory {

	/**
	 * 查询建议器输入控件类型
	 */
	static TYPE: string = "suggestor";

	/**
	 * 获取查询建议器输入控件类型
	 */
	getType(): string {
		return SuggestorWidgetFactory.TYPE;
	}

	/**
	 * 生成查询建议器输入控件
	 *
	 * @param widget 查询建议器输入控件定义属性
	 */
	create(widget: SuggestorWidgetProps): VNode {
		return h(SuggestorWidget, {widget: widget});
	}

	/**
	 * 查询建议器输入控件校验规则
	 */
	getRule(widget: CheckWidgetProps): FormItemRule {
		return {
			required: true,
			message: '请选择' + widget.title,
			trigger: ['blur', 'input'],
		}
	}
}