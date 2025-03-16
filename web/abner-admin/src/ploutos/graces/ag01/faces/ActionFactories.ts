/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {VNode} from "vue";
/********************************************************************************
 * 动作按钮注册工厂
 *
 * @author Berlin
 *******************************************************************************/
import type ActionFactory from "@/ploutos/graces/ag01/faces/ActionFactory.ts";
import type AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction.ts";
import LinkActionFactory from "@/ploutos/graces/ag01/faces/actions/LinkActionFactory";
import ScriptLinkActionFactory from "@/ploutos/graces/ag01/faces/actions/ScriptLinkActionFactory.ts";
import CheckLinkActionFactory from "@/ploutos/graces/ag01/faces/actions/CheckLinkActionFactory.ts";

export default class ActionFactories {

	/**
	 * 实例
	 */
	private static instance: ActionFactories = new ActionFactories();

	/**
	 * 所有注册的动作按钮工厂
	 */
	private factories = new Map<String, ActionFactory>();

	/**
	 * 动作按钮工厂
	 */
	constructor() {
		this.registry(new LinkActionFactory());
		this.registry(new ScriptLinkActionFactory());
		this.registry(new CheckLinkActionFactory())
	}

	/**
	 * 获取实例
	 */
	static getInstance(): ActionFactories {
		return this.instance;
	}

	/**
	 * 注册动作按钮工厂
	 *
	 * @param factory 动作按钮生产工厂
	 */
	private registry(factory: ActionFactory) {
		this.factories.set(factory.getType(), factory);
	}

	/**
	 * 获取动作按钮工厂
	 *
	 * @param type 动作按钮类型
	 */
	private get(type: string): ActionFactory {
		return this.factories.get(type);
	}

	/**
	 * 生成动作按钮
	 *
	 * @param action 动作按钮定义属性
	 */
	create(action: AbstractAction): VNode {
		return this.get(action.type).create(action);
	}
}