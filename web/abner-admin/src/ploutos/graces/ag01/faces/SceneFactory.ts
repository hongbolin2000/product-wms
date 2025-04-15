/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {VNode} from "vue";
/********************************************************************************
 * 表单显示控件工厂定义
 *
 * @author Berlin
 *******************************************************************************/
import type AbstractScene from "@/ploutos/graces/ag01/faces/AbstractScene";

export default interface SceneFactory {

	/**
	 * 控件类型
	 */
	getType(): string;

	/**
	 * 生成控件
	 *
	 * @param scene 控件定义属性
	 */
	create(scene: AbstractScene): VNode;
}