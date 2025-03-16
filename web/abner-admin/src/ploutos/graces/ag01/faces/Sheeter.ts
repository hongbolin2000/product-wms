/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 编辑表格属性定义
 *
 * @author Berlin
 *******************************************************************************/
import type AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget";
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";
import type {Ref} from "vue";

export default class Sheeter extends AbstractComponent {

	/**
	 * 编辑表格宽度
	 */
	width: string;

	/**
	 * 编辑表格最大高度
	 */
	maxHeight: string;

	/**
	 * 输入控件
	 */
	widgets: AbstractWidget[];

	/**
	 * 是否选项卡显示
	 */
	tab: boolean;

	/**
	 * 是否可新增数据
	 */
	added: boolean;

	/**
	 * 是否必填
	 */
	required: boolean;

	/**
	 * 是否折叠
	 */
	collapse: Ref<boolean> | boolean;

	/**
	 * 编辑表格数据
	 */
	data: object[];
}