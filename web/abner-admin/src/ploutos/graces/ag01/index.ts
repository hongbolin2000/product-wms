/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 通用界面
 *
 * @author Berlin
 *******************************************************************************/
import createGridViewer from '@/ploutos/graces/ag01/grid-viewer/index.vue';
import createFormEditor from '@/ploutos/graces/ag01/form-editor/index.vue';
import SorterProps from "@/ploutos/graces/ag01/faces/Sorter.ts";

export namespace Graces {

	/**
	 * 通用浏览表格界面
	 */
	export const GridViewer = createGridViewer;

	/**
	 * 通用编辑表单界面
	 */
	export const FormEditor = createFormEditor;

	/**
	 * 排序类型
	 */
	export type Sorter = SorterProps;
}
export default Graces;