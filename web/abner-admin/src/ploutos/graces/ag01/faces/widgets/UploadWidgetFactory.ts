/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {h, type VNode} from "vue";
import type {FormItemRule} from "naive-ui/es/form/src/interface";
/********************************************************************************
 * 文件上传输入控件工厂
 *
 * @author Berlin
 *******************************************************************************/
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import UploadWidget from "@/ploutos/graces/ag01/faces/widgets/UploadWidget.vue";
import type UploadWidgetProps from "@/ploutos/graces/ag01/faces/widgets/UploadWidgetProps.ts";

export default class UploadWidgetFactory implements WidgetFactory {

	/**
	 * 选择文件上传控件类型
	 */
	static TYPE: string = "upload";

	/**
	 * 获取选择输入控件类型
	 */
	getType(): string {
		return UploadWidgetFactory.TYPE;
	}

	/**
	 * 生成选择输入控件
	 *
	 * @param widget 选择输入控件定义属性
	 */
	create(widget: UploadWidgetProps): VNode {
		return h(UploadWidget, {widget: widget});
	}

	/**
	 * 查询建议器输入控件校验规则
	 */
	getRule(widget: UploadWidgetProps): FormItemRule {
		return {
			required: true,
			message: '请上传' + widget.title,
			trigger: ['blur', 'input'],
		}
	}
}