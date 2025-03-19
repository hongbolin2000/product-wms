/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
/********************************************************************************
 * 文件上传动作按钮工厂
 *
 * @author Berlin
 *******************************************************************************/
import type ActionFactory from "@/ploutos/graces/ag01/faces/ActionFactory.ts";
import type UploadActionProps from "@/ploutos/graces/ag01/faces/actions/UploadActionProps.ts";
import UploadAction from "@/ploutos/graces/ag01/faces/actions/UploadAction.vue";

export default class UploadActionFactory implements ActionFactory {

	/**
	 * 动作按钮类型
	 */
	static TYPE: string = "upload";

	/**
	 * 获取动作按钮类型
	 */
	getType(): string {
		return UploadActionFactory.TYPE;
	}

	/**
	 * 生成动作按钮
	 *
	 * @param action 动作按钮定义属性
	 */
	create(action: UploadActionProps): VNode {
		return h(UploadAction, {action: action});
	}
}