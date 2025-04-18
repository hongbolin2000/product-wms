/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 文本显示控件工厂
 *
 * @author Berlin
 *******************************************************************************/
import type SceneFactory from "@/ploutos/graces/ag01/faces/SceneFactory.ts";
import LabelScene from "@/ploutos/graces/ag01/faces/scenes/LabelScene.vue";
import AbstractScene from "@/ploutos/graces/ag01/faces/AbstractScene.ts";

export default class LabelSceneFactory implements SceneFactory {

	/**
	 * 控件类型
	 */
	static TYPE: string = "label";

	/**
	 * 获取控件类型
	 */
	getType(): string {
		return LabelSceneFactory.TYPE;
	}

	/**
	 * 生成控件
	 *
	 * @param scene 控件定义属性
	 */
	create(scene: AbstractScene): VNode {
		return h(LabelScene, {scene: scene});
	}
}