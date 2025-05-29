/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {defineStore} from "pinia";
import {ref, type Ref} from "vue";
import type EditorProps from "@/ploutos/graces/ag01/form-editor/EditorProps.ts";
/********************************************************************************
 * 通用界面全局应用状态
 *
 * @author Berlin
 ********************************************************************************/

/**
 * 创建通用界面全局应用状态
 */
const useGracesStore = defineStore('gracesStore', () => {

	/**
	 * 状态定义
	 */
	const store: GracesStoreOption = {
		editors: ref(new Map()),
	}

	/**
	 * 将编辑表单定义注册到全局状态中
	 *
	 * @param module 模块号
	 * @param name 通用界面名称
	 * @param editor 编辑表单定义
	 */
	function registryEditor(module: string, name: string, editor: EditorProps): void {
		store.editors.value.set(getName(module, name), editor);
	}

	/**
	 * 从全局状态中获取编辑表单定义
	 *
	 * @param module 模块号
	 * @param name 通用界面名称
	 */
	function getEditor(module: string, name: string): EditorProps {
		return store.editors.value.get(getName(module, name));
	}

	/**
	 * 获取格式化的通用界面名称
	 */
	function getName(module: string, name: string) {
		return module + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	return { ...store, registryEditor, getEditor }
});
export default useGracesStore;

/**
 * 通用界面应用状态类型定义
 */
type GracesStoreOption = {

	/**
	 * 编辑表单
	 */
	editors: Ref<Map<string, EditorProps>>;
}
