/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 编辑表单属性定义
 *
 * @author Berlin
 *******************************************************************************/
import type AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget";
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";
import type {Ref} from "vue";

export default class FormEditor extends AbstractComponent {

	/**
	 * 每行显示控件数
	 */
	spans: number;

	/**
	 * 输入控件
	 */
	widgets: AbstractWidget[];

	/**
	 * 是否选项卡显示
	 */
	tab: boolean;

	/**
	 * 表单面板宽度
	 */
	width: string;

	/**
	 * 表单宽度
	 */
	formWidth: string;

	/**
	 * 标签显示位置
	 */
	placement: 'left' | 'top';

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 是否折叠
	 */
	collapse: Ref<boolean> | boolean;
}