/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
/********************************************************************************
 * 表格列定义
 *
 * @author Berlin
 *******************************************************************************/
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";
import ValueModel from "@/ploutos/graces/ag01/faces/ValueModel";
import type {Ref} from "vue";

export default class AbstractColumn extends AbstractComponent {

	/**
	 * 列宽度
	 */
	width: number;

	/**
	 * 过滤器
	 */
	filter: string;

	/**
	 * 过滤选项
	 */
	filterOptions: ValueModel[]

	/**
	 * 当前行数据
	 */
	rowData: any;

	/**
	 * 当前行索引
	 */
	rowIndex: number;
}