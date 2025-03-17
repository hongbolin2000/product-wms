/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 日期输入控件属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";

export default class DateWidgetProps extends AbstractWidget {

	/**
	 * 输入模式（date,datetime,daterange,month,year,quarter,week）
	 */
	mode: 'date'| 'daterange' | 'datetime' | 'datetimerange' | 'month' | 'monthrange' | 'year'
		| 'yearrange' | 'quarter' | 'quarterrange' | 'week';

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 值格式化
	 */
	valueFormat: string;
}