/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 文件展示列工厂
 *
 * @author Berlin
 *******************************************************************************/
import type SceneFactory from "@/ploutos/graces/ag01/faces/SceneFactory.ts";
import type FileSceneProps from "@/ploutos/graces/ag01/faces/scenes/FileSceneProps.ts";
import FileScene from "@/ploutos/graces/ag01/faces/scenes/FileScene.vue";

export default class FileSceneFactory implements SceneFactory {

	/**
	 * 文件展示列类型
	 */
	static TYPE: string = "file";

	/**
	 * 获取文件展示列类型
	 */
	getType(): string {
		return FileSceneFactory.TYPE;
	}

	/**
	 * 生成文件展示列
	 */
	create(scene: FileSceneProps): VNode {
		return h(FileScene, {scene: scene});
	}
}