/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {VNode} from "vue";
/********************************************************************************
 * 表单显示控件注册工厂
 *
 * @author Berlin
 *******************************************************************************/
import type SceneFactory from "@/ploutos/graces/ag01/faces/SceneFactory.ts";
import type AbstractScene from "@/ploutos/graces/ag01/faces/AbstractScene.ts";
import LabelSceneFactory from "@/ploutos/graces/ag01/faces/scenes/LabelSceneFactory.ts";
import TagSceneFactory from "@/ploutos/graces/ag01/faces/scenes/TagSceneFactory.ts";
import FileSceneFactory from "@/ploutos/graces/ag01/faces/scenes/FileSceneFactory.ts";

export default class SceneFactories {

	/**
	 * 实例
	 */
	private static instance: SceneFactories = new SceneFactories();

	/**
	 * 所有注册的控件工厂
	 */
	private factories = new Map<String, SceneFactory>();

	/**
	 * 控件工厂
	 */
	constructor() {
		this.registry(new LabelSceneFactory());
		this.registry(new TagSceneFactory());
		this.registry(new FileSceneFactory())
	}

	/**
	 * 获取实例
	 */
	static getInstance() {
		return this.instance;
	}

	/**
	 * 注册控件工厂
	 *
	 * @param factory 控件生产工厂
	 */
	private registry(factory: SceneFactory) {
		this.factories.set(factory.getType(), factory);
	}

	/**
	 * 获取控件工厂
	 *
	 * @param type 控件类型
	 */
	private get(type: string): SceneFactory {
		return this.factories.get(type);
	}

	/**
	 * 生成控件
	 *
	 * @param widget 控件定义属性
	 */
	create(widget: AbstractScene): VNode {
		return this.get(widget.type).create(widget);
	}
}