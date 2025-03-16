/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {Parser} from "expr-eval";
import type AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";
/********************************************************************************
 * 输入控件工具类
 *
 * @author Berlin
 *******************************************************************************/
export namespace WidgetUtil {

	/**
	 * 检测输入控件是否禁用
	 */
	export function disabled(currentWidget: AbstractWidget) {
		// 没有数据时直接返回
		if (Object.getOwnPropertyNames(currentWidget.rowData).length <= 0) {
			return;
		}

		// 根据当前输入框设置的表达式计算是否禁用
		if (currentWidget.disabled) {
			currentWidget.isDisabled = Parser.parse(currentWidget.disabled).evaluate(currentWidget.rowData);
		}

		// 根据当前输入输入框的值计算其他输入控件是否禁用
		currentWidget.widgets.forEach(widget => {
			if (widget.name != currentWidget.name && widget.disabled &&
				new RegExp(currentWidget.name).test(widget.disabled)) {
				widget.isDisabled = Parser.parse(widget.disabled).evaluate(currentWidget.rowData);
			}
		});
	}
}