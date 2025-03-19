/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 文件展示列工厂
 *
 * @author Berlin
 *******************************************************************************/
import type ColumnFactory from "@/ploutos/graces/ag01/faces/ColumnFactory.ts";
import type FileColumnProps from "@/ploutos/graces/ag01/faces/columns/FileColumnProps.ts";
import FileColumn from "@/ploutos/graces/ag01/faces/columns/FileColumn.vue";

export default class FileColumnFactory implements ColumnFactory {

	/**
	 * 文件展示列类型
	 */
	static TYPE: string = "file";

	/**
	 * 获取文件展示列类型
	 */
	getType(): string {
		return FileColumnFactory.TYPE;
	}

	/**
	 * 生成文件展示列
	 */
	create(column: FileColumnProps): VNode {
		return h(FileColumn, {column: column});
	}
}