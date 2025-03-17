/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 枚举输入控件属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";
import type ValueModel from "@/ploutos/graces/ag01/faces/ValueModel.ts";

export default class EnumWidgetProps extends AbstractWidget {

	/**
	 * 枚举选项
	 */
	options: ValueModel[];

	/**
	 * 选择模式(select,radio)
	 */
	mode: 'select' | 'radio'
}