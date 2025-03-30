/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {VNodeChild} from "vue";
/********************************************************************************
 * 布局框架中的类型定义
 *
 * @author Berlin
 ********************************************************************************/

/**
 * 菜单属性
 */
export type MenuOption = {

  /**
   * 菜单ID
   */
  id?: string;

  /**
   * 菜单Key
   */
  key: string,

  /**
   * 菜单访问路径
   */
  url?: string;

  /**
   * 菜单全路径(从父菜单到子菜单拼接)
   */
  fullUrl: string;

  /**
   * 标签
   */
  label?: string,

  /**
   * 菜单字符图标
   */
  icons?: string,

  /**
   * 菜单渲染图标
   */
  icon?: () => VNodeChild;

  /**
   * 父菜单图标
   */
  parentIcon?: string,

  /**
   * 是否固定菜单在选项卡中
   */
  fixed?: boolean;

  /**
   * 子菜单
   */
  children?: MenuOption[],

  /**
   * 是否选中(顶+左混合模式)
   */
  checked?: boolean,
};

/**
 * 网站信息
 */
export type WebsiteOption = {

  /**
   * 公司名称
   */
  companyName: string,

  /**
   * 网站标题
   */
  websiteTitle: string,

  /**
   * 副标题
   */
  websiteSubtitle: string,

  /**
   * 登录验证码
   */
  captchaVerify: true,

  /**
   * 7天免登录
   */
  autoLogin: true,

  /**
   * 记住账号
   */
  rememberAccount: true,

  /**
   * 记住密码
   */
  rememberPassword: true;
}

/**
 * 布局模式
 */
export enum LayoutMode {

  /**
   * 左右
   */
  LeftRight = 'ltr',

  /**
   * 上下
   */
  TopBottom = 'ttb',

  /**
   * 分栏
   */
  LeftSplit = 'lcr',

  /**
   * 顶部+左侧混合
   */
  TopLeft = 'tlf'
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
