/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type Sheeter from "@/ploutos/graces/ag01/faces/Sheeter.ts";

/********************************************************************************
 * 编辑表格行属性
 *
 * @author Berlin
 *******************************************************************************/
export default class SheeterRow {

	/**
	 * 一行展示的表格数
	 */
	sheeterCount: number;

	/**
	 * 行下的所有编辑表格
	 */
	sheeters: Sheeter[];

	/**
	 * 非选项卡展示的编辑表格
	 */
	noTabSheeters: Sheeter[];

	/**
	 * 选项卡展示的编辑表单
	 */
	tabSheeters: Sheeter[];
}