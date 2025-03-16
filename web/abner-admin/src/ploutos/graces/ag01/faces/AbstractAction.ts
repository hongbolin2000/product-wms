/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
/********************************************************************************
 * 动作按钮定义
 *
 * @author Berlin
 *******************************************************************************/
import AbstractComponent from "@/ploutos/graces/ag01/faces/AbstractComponent";
import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";

export default class AbstractAction extends AbstractComponent {

	/**
	 * 图标
	 */
	icon: string;

	/**
	 * 是否显示在更多选项中
	 */
	option: boolean;

	/**
	 * 危险操作
	 */
	danger: boolean;

	/**
	 * 当前按钮所处的数据表格
	 */
	datatable: Datatable;
}