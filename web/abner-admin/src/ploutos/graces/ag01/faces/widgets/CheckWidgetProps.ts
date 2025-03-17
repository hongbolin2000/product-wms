/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 选择输入控件属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";

export default class CheckWidgetProps extends AbstractWidget {

	/**
	 * 选中时的值
	 */
	checked: string;

	/**
	 * 未选中时的值
	 */
	unchecked: string;
}