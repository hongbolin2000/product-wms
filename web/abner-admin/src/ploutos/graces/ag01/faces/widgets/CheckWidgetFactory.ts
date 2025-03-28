/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 选择输入控件工厂
 *
 * @author Berlin
 *******************************************************************************/
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import  CheckWidgetProps from "@/ploutos/graces/ag01/faces/widgets/CheckWidgetProps.ts";
import CheckWidget from "@/ploutos/graces/ag01/faces/widgets/CheckWidget.vue";
import type {FormItemRule} from "naive-ui/es/form/src/interface";

export default class CheckWidgetFactory implements WidgetFactory {

	/**
	 * 选择输入控件类型
	 */
	static TYPE: string = "check";

	/**
	 * 获取选择输入控件类型
	 */
	getType(): string {
		return CheckWidgetFactory.TYPE;
	}

	/**
	 * 生成选择输入控件
	 *
	 * @param widget 选择输入控件定义属性
	 */
	create(widget: CheckWidgetProps): VNode {
		return h(CheckWidget, {widget: widget});
	}

	/**
	 * 选择输入控件校验规则
	 */
	getRule(widget: CheckWidgetProps): FormItemRule {
		return {
			required: true,
			message: '请选择' + widget.title,
			trigger: ['blur', 'input'],
		}
	}
}