/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {VNode} from "vue";
/********************************************************************************
 * 动作按钮工厂定义
 *
 * @author Berlin
 *******************************************************************************/
import type AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction.ts";

export default interface ActionFactory {

	/**
	 * 动作按钮类型
	 */
	getType(): string;

	/**
	 * 生成动作按钮
	 *
	 * @param action 动作按钮定义属性
	 */
	create(action: AbstractAction): VNode;
}