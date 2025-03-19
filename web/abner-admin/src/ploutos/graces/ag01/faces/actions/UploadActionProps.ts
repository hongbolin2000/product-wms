/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 文件上传动作按钮属性定义
 *
 * @author Berlin
 *******************************************************************************/
import AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction";

/**
 * 路由动作按钮属性
 */
export default class UploadActionProps extends AbstractAction {

	/**
	 * 文件上传地址
	 */
	link: string;

	/**
	 * 接受的文件类型
	 */
	accept: string;
}