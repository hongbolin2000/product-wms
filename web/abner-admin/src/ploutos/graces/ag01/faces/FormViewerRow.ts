/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 浏览表单行属性
 *
 * @author Berlin
 *******************************************************************************/
import type FormViewer from "@/ploutos/graces/ag01/faces/FormViewer";

export default class FormEditorRow {

	/**
	 * 行下的所有浏览表单
	 */
	editors: FormViewer[];

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 一行展示的表单数
	 */
	viewerCount: number;

	/**
	 * 非选项卡展示的浏览表单
	 */
	noTabViewers: FormViewer[];

	/**
	 * 选项卡展示的浏览表单
	 */
	tabViewers: FormViewer[];
}