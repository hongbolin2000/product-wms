/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 文件展示列属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";

export default class FileColumnProps extends AbstractColumn {

	/**
	 * 文件展现模式(text,image)
	 */
	mode: 'text' | 'image';
}