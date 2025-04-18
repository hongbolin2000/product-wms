/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * tag标签列工厂
 *
 * @author Berlin
 *******************************************************************************/
import type SceneFactory from "@/ploutos/graces/ag01/faces/SceneFactory.ts";
import TagScene from "@/ploutos/graces/ag01/faces/scenes/TagScene.vue";
import type TagSceneProps from "@/ploutos/graces/ag01/faces/scenes/TagSceneProps.ts";

export default class TagSceneFactory implements SceneFactory {

	/**
	 * tag标签列类型
	 */
	static TYPE: string = "tag";

	/**
	 * 获取tag标签列类型
	 */
	getType(): string {
		return TagSceneFactory.TYPE;
	}

	/**
	 * 生成标签列
	 */
	create(scene: TagSceneProps): VNode {
		return h(TagScene, {scene: scene});
	}
}