/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 文件展示列属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractScene from "@/ploutos/graces/ag01/faces/AbstractScene.ts";

export default class FileSceneProps extends AbstractScene {

	/**
	 * 文件展现模式(text,image)
	 */
	mode: 'text' | 'image';
}