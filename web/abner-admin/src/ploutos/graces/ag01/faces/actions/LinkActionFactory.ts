/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 路由动作按钮工厂
 *
 * @author Berlin
 *******************************************************************************/
import ActionFactory from "@/ploutos/graces/ag01/faces/ActionFactory";
import AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction";
import LinkAction from "@/ploutos/graces/ag01/faces/actions/LinkAction.vue";

export default class LinkActionFactory implements ActionFactory {

	/**
	 * 动作按钮类型
	 */
	static TYPE: string = "link";

	/**
	 * 获取动作按钮类型
	 */
	getType(): string {
		return LinkActionFactory.TYPE;
	}

	/**
	 * 生成动作按钮
	 *
	 * @param action 动作按钮定义属性
	 */
	create(action: AbstractAction): VNode {
		return h(LinkAction, {action: action});
	}
}