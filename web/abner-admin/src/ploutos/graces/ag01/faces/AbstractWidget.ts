/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 输入控件属性定义
 *
 * @author Berlin
 *******************************************************************************/
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";
import type {Ref} from "vue";

export default class AbstractWidget extends AbstractComponent{

	/**
	 * 控件占用列数
	 */
	spans: number;

	/**
	 * 是否必填
	 */
	required: boolean;

	/**
	 * 缺省值
	 */
	default: string;

	/**
	 * 控件宽度
	 */
	width: string;

	/**
	 * 禁用表达式
	 */
	disabled: string;

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 表单校验规则
	 */
	formRules: Ref<object>;

	/**
	 * 是否禁用
	 */
	isDisabled: boolean;

	/**
	 * 表单值
	 */
	rowData: any;

	/**
	 * 当前表单所有的输入控件
	 */
	widgets: AbstractWidget[];
}