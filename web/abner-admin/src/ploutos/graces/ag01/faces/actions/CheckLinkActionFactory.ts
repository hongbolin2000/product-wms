/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 选择项动作按钮工厂
 *
 * @author Berlin
 *******************************************************************************/
import type ActionFactory from "@/ploutos/graces/ag01/faces/ActionFactory.ts";
import CheckLinkAction from "@/ploutos/graces/ag01/faces/actions/CheckLinkAction.vue";
import type CheckLinkActionProps from "@/ploutos/graces/ag01/faces/actions/CheckLinkActionProps.ts";

export default class CheckLinkActionFactory implements ActionFactory {

	/**
	 * 动作按钮类型
	 */
	static TYPE: string = "checkLink";

	/**
	 * 获取动作按钮类型
	 */
	getType(): string {
		return CheckLinkActionFactory.TYPE;
	}

	/**
	 * 生成动作按钮
	 *
	 * @param action 动作按钮定义属性
	 */
	create(action: CheckLinkActionProps): VNode {
		return h(CheckLinkAction, {action: action});
	}
}