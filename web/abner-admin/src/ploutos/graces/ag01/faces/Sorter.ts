/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 排序字段属性定义
 *
 * @author Berlin
 *******************************************************************************/
export default class Sorter {

	/**
	 * 排序字段
	 */
	name: string | number;

	/**
	 * 排序方式
	 */
	order: 'asc' | 'desc'
}