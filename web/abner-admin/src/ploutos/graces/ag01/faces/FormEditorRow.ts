/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 编辑表单行属性
 *
 * @author Berlin
 *******************************************************************************/
import type FormEditor from "@/ploutos/graces/ag01/faces/FormEditor.ts";

export default class FormEditorRow {

	/**
	 * 一行展示的表单数
	 */
	editorCount: number;

	/**
	 * 行下的所有编辑表单
	 */
	editors: FormEditor[];

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 非选项卡展示的编辑表单
	 */
	noTabEditors: FormEditor[];

	/**
	 * 选项卡展示的编辑表单
	 */
	tabEditors: FormEditor[];
}