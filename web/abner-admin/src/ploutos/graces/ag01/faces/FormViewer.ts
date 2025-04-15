/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 浏览表单属性定义
 *
 * @author Berlin
 *******************************************************************************/
import type AbstractScene from "@/ploutos/graces/ag01/faces/AbstractScene";
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";
import type {Ref} from "vue";

export default class FormEditor extends AbstractComponent {

	/**
	 * 友好提示的列
	 */
	labelColumn: string;

	/**
	 * 每行显示控件数
	 */
	spans: number;

	/**
	 * 显示控件
	 */
	scenes: AbstractScene[];

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