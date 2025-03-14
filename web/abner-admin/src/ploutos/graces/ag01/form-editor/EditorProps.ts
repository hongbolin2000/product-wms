/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 通用编辑表单属性定义
 *
 * @author Berlin
 *******************************************************************************/
import FormEditorRow from "@/ploutos/graces/ag01/faces/FormEditorRow";
import type Sheeter from "@/ploutos/graces/ag01/faces/Sheeter.ts";
import type SheeterRow from "@/ploutos/graces/ag01/faces/SheeterRow.ts";

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
	 * 编辑表单
	 */
	editorRows: FormEditorRow[];

	/**
	 * 编辑表格
	 */
	sheeterRows: SheeterRow[];
}