/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 脚本动作按钮工厂
 *
 * @author Berlin
 *******************************************************************************/
import type ActionFactory from "@/ploutos/graces/ag01/faces/ActionFactory.ts";
import ScriptLinkAction from "@/ploutos/graces/ag01/faces/actions/ScriptLinkAction.vue";
import type ScriptLinkActionProps from "@/ploutos/graces/ag01/faces/actions/ScriptLinkActionProps.ts";

export default class ScriptLinkActionFactory implements ActionFactory {

	/**
	 * 动作按钮类型
	 */
	static TYPE: string = "scriptLink";

	/**
	 * 获取动作按钮类型
	 */
	getType(): string {
		return ScriptLinkActionFactory.TYPE;
	}

	/**
	 * 生成动作按钮
	 *
	 * @param action 动作按钮定义属性
	 */
	create(action: ScriptLinkActionProps): VNode {
		return h(ScriptLinkAction, {action: action});
	}
}