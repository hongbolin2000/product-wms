/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {VNode} from "vue";
/********************************************************************************
 * 输入控件注册工厂
 *
 * @author Berlin
 *******************************************************************************/
import type WidgetFactory from "@/ploutos/graces/ag01/faces/WidgetFactory.ts";
import type AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";
import TextWidgetFactory from "@/ploutos/graces/ag01/faces/widgets/TextWidgetFactory";
import NumberWidgetFactory from "@/ploutos/graces/ag01/faces/widgets/NumberWidgetFactory.ts";
import DateWidgetFactory from "@/ploutos/graces/ag01/faces/widgets/DateWidgetFactory.ts";
import TimeWidgetFactory from "@/ploutos/graces/ag01/faces/widgets/TimeWidgetFactory.ts";
import EnumWidgetFactory from "@/ploutos/graces/ag01/faces/widgets/EnumWidgetFactory.ts";
import CheckWidgetFactory from "@/ploutos/graces/ag01/faces/widgets/CheckWidgetFactory.ts";
import SuggestorWidgetFactory from "@/ploutos/graces/ag01/faces/widgets/SuggestorWidgetFactory.ts";
import UploadWidgetFactory from "@/ploutos/graces/ag01/faces/widgets/UploadWidgetFactory.ts";

export default class WidgetFactories {

	/**
	 * 实例
	 */
	private static instance: WidgetFactories = new WidgetFactories();

	/**
	 * 所有注册的输入控件工厂
	 */
	private factories = new Map<String, WidgetFactory>();

	/**
	 * 输入控件工厂
	 */
	constructor() {
		this.registry(new TextWidgetFactory());
		this.registry(new NumberWidgetFactory());
		this.registry(new DateWidgetFactory());
		this.registry(new TimeWidgetFactory());
		this.registry(new EnumWidgetFactory());
		this.registry(new CheckWidgetFactory());
		this.registry(new SuggestorWidgetFactory());
		this.registry(new UploadWidgetFactory());
	}

	/**
	 * 获取实例
	 */
	static getInstance() {
		return this.instance;
	}

	/**
	 * 注册输入控件工厂
	 *
	 * @param factory 输入控件生产工厂
	 */
	private registry(factory: WidgetFactory) {
		this.factories.set(factory.getType(), factory);
	}

	/**
	 * 获取输入控件工厂
	 *
	 * @param type 表格列类型
	 */
	private get(type: string): WidgetFactory {
		return this.factories.get(type);
	}

	/**
	 * 生成输入控件
	 *
	 * @param widget 输入控件定义属性
	 */
	create(widget: AbstractWidget): VNode {
		return this.get(widget.type).create(widget);
	}
}