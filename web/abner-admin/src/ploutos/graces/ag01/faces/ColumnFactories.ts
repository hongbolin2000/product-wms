/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {VNode} from "vue";
/********************************************************************************
 * 表格列注册工厂
 *
 * @author Berlin
 *******************************************************************************/
import type ColumnFactory from "@/ploutos/graces/ag01/faces/ColumnFactory.ts";
import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";
import LinkColumnFactory from "@/ploutos/graces/ag01/faces/columns/LinkColumnFactory.ts";
import ScriptLinkColumnFactory from "@/ploutos/graces/ag01/faces/columns/ScriptLinkColumnFactory.ts";
import TagColumnFactory from "@/ploutos/graces/ag01/faces/columns/TagColumnFactory.ts";
import FileColumnFactory from "@/ploutos/graces/ag01/faces/columns/FileColumnFactory.ts";
import LabelColumnFactory from "@/ploutos/graces/ag01/faces/columns/LabelColumnFactory.ts";

export default class ColumnFactories {

	/**
	 * 实例
	 */
	private static instance: ColumnFactories = new ColumnFactories();

	/**
	 * 所有注册的表格列工厂
	 */
	private factories = new Map<String, ColumnFactory>();

	/**
	 * 表格列工厂
	 */
	constructor() {
		this.registry(new LabelColumnFactory());
		this.registry(new LinkColumnFactory());
		this.registry(new ScriptLinkColumnFactory());
		this.registry(new TagColumnFactory());
		this.registry(new FileColumnFactory());
	}

	/**
	 * 获取实例
	 */
	static getInstance() {
		return this.instance;
	}

	/**
	 * 注册表格列工厂
	 *
	 * @param factory 表格列生产工厂
	 */
	private registry(factory: ColumnFactory) {
		this.factories.set(factory.getType(), factory);
	}

	/**
	 * 获取表格列工厂
	 *
	 * @param type 表格列类型
	 */
	private get(type: string): ColumnFactory {
		return this.factories.get(type);
	}

	/**
	 * 生成表格列
	 *
	 * @param column 表格列定义属性
	 */
	create(column: AbstractColumn): VNode {
		return this.get(column.type).create(column);
	}

	/**
	 * 是否link
	 */
	static isLink(column: AbstractColumn): boolean {
		return LinkColumnFactory.TYPE == column.type || ScriptLinkColumnFactory.TYPE == column.type;
	}
}