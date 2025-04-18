/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * tag标签列属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractScene from "@/ploutos/graces/ag01/faces/AbstractScene.ts";

export default class TagSceneProps extends AbstractScene {

	/**
	 * 成功标签展示表达式
	 */
	success: string;

	/**
	 * 警告标签展示表达式
	 */
	warning: string;

	/**
	 * 异常标签展示表达式
	 */
	error: string;

	/********************************************************************************
	 * 以下为前端计算内容
	 *******************************************************************************/
	/**
	 * 标签类型
	 */
	tagType: 'primary' | 'success' | 'warning' | 'error';
}