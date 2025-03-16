/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 通用编辑表单属性定义
 *
 * @author Berlin
 *******************************************************************************/
import FormEditorRow from "@/ploutos/graces/ag01/faces/FormEditorRow";
import type SheeterRow from "@/ploutos/graces/ag01/faces/SheeterRow.ts";
import type FormEditor from "@/ploutos/graces/ag01/faces/FormEditor.ts";

export default class EditorProps {

	/**
	 * 新增时显示的标题
	 */
	atitle: string;

	/**
	 * 编辑时显示的标题
	 */
	etitle: string;

	/**
	 * 提交时调用的服务器地址(如果设置了则直接调用服务器，否则使用JavaScript)
	 */
	url: string;

	/**
	 * 编辑表单
	 */
	editorRows: FormEditorRow[];

	/**
	 * 编辑表格
	 */
	sheeterRows: SheeterRow[];

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 所有的编辑表单
	 */
	allEditors: FormEditor[];
}