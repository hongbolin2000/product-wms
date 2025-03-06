/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
/********************************************************************************
 * 动作按钮定义
 *
 * @author Berlin
 *******************************************************************************/
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";

export default class AbstractAction extends AbstractComponent {

	/**
	 * 图标
	 */
	icon: string;

	/**
	 * 标签
	 */
	label: string;
}