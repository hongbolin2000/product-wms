/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 文件上传输入控件属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";

export default class UploadWidgetProps extends AbstractWidget {

	/**
	 * 图片存储分组路径
	 */
	group: string;

	/**
	 * 展现模式(text,image,card,dragger)
	 */
	mode: 'text' | 'image' | 'card' | 'dragger';

	/**
	 * 接受的文件类型
	 */
	accept: string;
}