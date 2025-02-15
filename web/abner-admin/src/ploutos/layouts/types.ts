/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {MenuOptionSharedPart} from "naive-ui/es/menu/src/interface";
import type {VNode} from "vue";
/********************************************************************************
 * 布局框架中的类型定义
 *
 * @author Berlin
 ********************************************************************************/

/**
 * token名称
 */
export const TOKEN_NAME = "authorization";

/**
 * 菜单属性
 */
export type MenuOption = (MenuOptionSharedPart & {

  /**
   * 菜单图标
   */
  icons: string,

  /**
   * 子菜单
   */
  children: MenuOption[]
});

/**
 * 网站信息
 */
export type WebsiteOption = {

  /**
   * 网站标题
   */
  title: string;

  /**
   * 副标题
   */
  subtitle: string;

  /**
   * 公司名称
   */
  companyName: string,

  /**
   * 应用版本
   */
  version: string;
}

/**
 * 框架主题
 */
export enum ThemeMode {

  /**
   * 暗黑主题
   */
  DARK = 'dark',

  /**
   * 明亮主题
   */
  LIGHT = 'light'
}

/**
 * 终端类型
 */
export enum DeviceType {

  /**
   * 电脑端
   */
  PC = 'pc',

  /**
   * 平板端
   */
  PAD = 'pad',

  /**
   * 手机端
   */
  MOBILE = 'mobile',
}

/**
 * 侧边栏主题
 */
export enum SideTheme {

  /**
   * 暗黑
   */
  DARK = 'dark',

  /**
   * 纯白
   */
  WHITE = 'white',

  /**
   * 背景图
   */
  IMAGE = 'image',
}
