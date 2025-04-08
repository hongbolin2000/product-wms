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
import createDataTable from "@/ploutos/graces/ag01/components/DataTable.vue";

import Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";
import LabelColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";
import CheckColumn from "@/ploutos/graces/ag01/faces/columns/SelectionColumnProps.ts";
import Sorter from "@/ploutos/graces/ag01/faces/Sorter.ts";

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
	 * 数据表格
	 */
	export const DataTable = createDataTable;

	/**
	 * 数据表格属性
	 */
	export type DatatableProps = Datatable;
	export type LabelColumnProps = LabelColumn;
	export type CheckColumnProps = CheckColumn;

	/**
	 * 排序类型
	 */
	export type SorterProps = Sorter;
}
export default Graces;