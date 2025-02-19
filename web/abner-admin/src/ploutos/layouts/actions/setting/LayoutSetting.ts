/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {type Ref, ref} from "vue";
/********************************************************************************
 * 布局静态配置信息
 *
 * @author Berlin
 *******************************************************************************/
import LeftBg from '@/ploutos/layouts/sidebar/menu-bg.webp'
import {SideTheme, ThemeMode} from "@/ploutos/layouts/types";

/**
 * 菜单栏布局样式
 */
export const sideExampleList: Ref<LayoutStyleOption[]> = ref([
  {
    leftBg: '#000000',
    rightTopBg: '#000000',
    rightBottomBg: '#f5f5f5',
    checked: true,
    sideThemeId: SideTheme.WHITE,
    tipText: '明亮',
  },{
    leftBg: '#000000',
    rightTopBg: '#000000',
    rightBottomBg: '#f5f5f5',
    checked: false,
    sideThemeId: SideTheme.DARK,
    tipText: '暗黑',
  }, {
    leftBg: `url(${LeftBg})`,
    rightTopBg: '#000000',
    rightBottomBg: '#f5f5f5',
    checked: false,
    sideThemeId: SideTheme.IMAGE,
    tipText: '背景图',
  },
]);

/**
 * 布局模式
 */
export const layoutExampleList: Ref<LayoutStyleOption[]> = ref([
  {
    leftBg: '#000000',
    rightTopBg: '#d4d4d4',
    rightBottomBg: '#d4d4d4',
    checked: true,
    layoutId: 'ltr',
    tipText: '左侧',
  }, {
    leftBg: '#d4d4d4',
    rightTopBg: '#000000',
    rightBottomBg: '#d4d4d4',
    checked: false,
    layoutId: 'ttb',
    class: 'extra-class',
    tipText: '顶部',
  }, {
    leftBg: '#000000',
    rightTopBg: '#d4d4d4',
    rightBottomBg: '#d4d4d4',
    checked: false,
    layoutId: 'lcr',
    class: 'extra-class-1',
    tipText: '分栏',
  },
]);

/**
 * 主题颜色
 */
export const primaryColorList: Ref<LayoutSelectOption[]> = ref([
  {
    label: 'cyan',
    value: '#18a058',
    checked: false,
  },{
    label: 'blue',
    value: '#409eff',
    checked: true,
  },{
    label: 'red',
    value: '#F5222D',
    checked: false,
  },{
    label: 'purple',
    value: '#722ED1',
    checked: false,
  },{
    label: 'ee4f12',
    value: '#ee4f12',
    checked: false,
  },{
    label: '0096c7',
    value: '#0096c7',
    checked: false,
  },{
    label: 'ff9801',
    value: '#ff9801',
    checked: false,
  },{
    label: 'ff3d68',
    value: '#ff3d68',
    checked: false,
  },{
    label: '01c1d4',
    value: '#01c1d4',
    checked: false,
  },{
    label: '71efa3',
    value: '#71efa3',
    checked: false,
  },{
    label: '009688',
    value: '#009688',
    checked: false,
  },{
    label: '78dec7',
    value: '#78dec7',
    checked: false,
  },{
    label: '1768ac',
    value: '#1768ac',
    checked: false,
  },{
    label: '1427df',
    value: '#1427df',
    checked: false,
  },{
    label: '43c628',
    value: '#43c628',
    checked: false,
  },{
    label: 'ead41e',
    value: '#ead41e',
    checked: false,
  },{
    label: '22bd7c',
    value: '#22bd7c',
    checked: false,
  },{
    label: '9727bf',
    value: '#9727bf',
    checked: false,
  },
]);

/**
 * 主题属性
 */
export type LayoutStyleOption = {

  /**
   * 左背景
   */
  leftBg: string,

  /**
   * 右上背景
   */
  rightTopBg: string,

  /**
   * 右下背景
   */
  rightBottomBg: string,

  /**
   * 是否选中
   */
  checked: boolean,

  /**
   * 提示文本
   */
  tipText: string,

  /**
   * 主题id
   */
  themeId?: ThemeMode,

  /**
   * 侧边栏主题样式
   */
  sideThemeId?: SideTheme,

  /**
   * 追加样式类
   */
  class?: string,

  /**
   * 布局ID
   */
  layoutId?: string
}

/**
 * 选项属性
 */
export type LayoutSelectOption = {

  /**
   * 标签
   */
  label: string,

  /**
   * 值
   */
  value: string,

  /**
   * 是否选中
   */
  checked?: boolean
}
