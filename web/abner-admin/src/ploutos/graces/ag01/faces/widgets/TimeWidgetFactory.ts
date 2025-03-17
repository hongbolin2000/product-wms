/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 时间输入控件工厂
 *
 * @author Berlin
 *******************************************************************************/
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import type AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";
import TimeWidget from "@/ploutos/graces/ag01/faces/widgets/TimeWidget.vue";

export default class TimeWidgetFactory implements WidgetFactory {

	/**
	 * 时间输入控件类型
	 */
	static TYPE: string = "time";

	/**
	 * 获取时间输入控件类型
	 */
	getType(): string {
		return TimeWidgetFactory.TYPE;
	}

	/**
	 * 生成时间输入控件
	 *
	 * @param widget 时间输入控件定义属性
	 */
	create(widget: AbstractWidget): VNode {
		return h(TimeWidget, {widget: widget});
	}
}