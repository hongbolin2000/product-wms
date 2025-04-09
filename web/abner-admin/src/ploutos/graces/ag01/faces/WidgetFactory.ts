/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {VNode} from "vue";
import type {FormItemRule} from "naive-ui/es/form/src/interface";
/********************************************************************************
 * 输入控件工厂定义
 *
 * @author Berlin
 *******************************************************************************/
import type AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget";

export default interface WidgetFactory {

	/**
	 * 输入控件类型
	 */
	getType(): string;

	/**
	 * 生成输入控件
	 *
	 * @param widget 输入控件定义属性
	 */
	create(widget: AbstractWidget): VNode;

	/**
	 * 输入控件校验规则
	 */
	getRule(widget: AbstractWidget): FormItemRule | FormItemRule[];
}