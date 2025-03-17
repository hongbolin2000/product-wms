/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 日期输入控件工厂
 *
 * @author Berlin
 *******************************************************************************/
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import DateWidget from "@/ploutos/graces/ag01/faces/widgets/DateWidget.vue";
import DateWidgetProps from "@/ploutos/graces/ag01/faces/widgets/DateWidgetProps.ts";

export default class DateWidgetFactory implements WidgetFactory {

	/**
	 * 日期输入控件类型
	 */
	static TYPE: string = "date";

	/**
	 * 获取日期输入控件类型
	 */
	getType(): string {
		return DateWidgetFactory.TYPE;
	}

	/**
	 * 生成日期输入控件
	 *
	 * @param widget 日期输入控件定义属性
	 */
	create(widget: DateWidgetProps): VNode {
		return h(DateWidget, {widget: widget});
	}
}