/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {ref} from "vue";
/********************************************************************************
 * 布局静态配置信息
 *
 * @author Berlin
 *******************************************************************************/
import LeftBg from '@/layouts/sidebar/menu-bg.webp'

/**
 * 主题列表
 */
export const themeList = ref([
  {
    leftBg: '#ffffff',
    rightTopBg: '#ffffff',
    rightBottomBg: '#f5f5f5',
    checked: true,
    themeId: 'light',
    tipText: '明亮',
  },
  {
    leftBg: '#000000',
    rightTopBg: '#000000',
    rightBottomBg: '#333333',
    checked: false,
    themeId: 'dark',
    tipText: '暗黑',
  },
]);

/**
 * 菜单栏布局样式
 */
export const sideExampleList = ref([
  {
    leftBg: '#000000',
    rightTopBg: '#ffffff',
    rightBottomBg: '#f5f5f5',
    checked: false,
    themeId: 'dark',
    tipText: '暗黑',
  },
  {
    leftBg: '#ffffff',
    rightTopBg: '#ffffff',
    rightBottomBg: '#f5f5f5',
    checked: true,
    themeId: 'white',
    tipText: '明亮',
  },
  {
    leftBg: `url(${LeftBg})`,
    rightTopBg: '#ffffff',
    rightBottomBg: '#f5f5f5',
    checked: false,
    themeId: 'image',
    tipText: '背景图',
  },
]);

/**
 * 布局模式
 */
export const layoutExampleList = ref([
  {
    leftBg: '#000000',
    rightTopBg: '#d4d4d4',
    rightBottomBg: '#d4d4d4',
    checked: true,
    layoutId: 'ltr',
    tipText: '左右',
  },
  {
    leftBg: '#d4d4d4',
    rightTopBg: '#ffffff',
    rightBottomBg: '#d4d4d4',
    checked: false,
    layoutId: 'ttb',
    class: 'extra-class',
    tipText: '上下',
  },
  {
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
export const primaryColorList = ref([
  {
    name: 'cyan',
    value: '#18a058',
    checked: false,
  },
  {
    name: 'blue',
    value: '#409eff',
    checked: true,
  },
  {
    name: 'red',
    value: '#F5222D',
    checked: false,
  },
  {
    name: 'purple',
    value: '#722ED1',
    checked: false,
  },
  {
    name: 'ee4f12',
    value: '#ee4f12',
    checked: false,
  },
  {
    name: '0096c7',
    value: '#0096c7',
    checked: false,
  },
  {
    name: 'ff9801',
    value: '#ff9801',
    checked: false,
  },
  {
    name: 'ff3d68',
    value: '#ff3d68',
    checked: false,
  },
  {
    name: '01c1d4',
    value: '#01c1d4',
    checked: false,
  },
  {
    name: '71efa3',
    value: '#71efa3',
    checked: false,
  },
  {
    name: '171010',
    value: '#171010',
    checked: false,
  },
  {
    name: '78dec7',
    value: '#78dec7',
    checked: false,
  },
  {
    name: '1768ac',
    value: '#1768ac',
    checked: false,
  },
  {
    name: '1427df',
    value: '#1427df',
    checked: false,
  },
  {
    name: '43c628',
    value: '#43c628',
    checked: false,
  },
  {
    name: 'ead41e',
    value: '#ead41e',
    checked: false,
  },
  {
    name: '22bd7c',
    value: '#22bd7c',
    checked: false,
  },
  {
    name: '9727bf',
    value: '#9727bf',
    checked: false,
  },
]);

/**
 * 页面切换效果
 */
export const animateOptions = [
  {
    label: '渐隐渐现',
    value: 'opacity',
  },
  {
    label: '左右滑动',
    value: 'fade',
  },
  {
    label: '上下滑动',
    value: 'down',
  },
  {
    label: '缩放效果',
    value: 'scale',
  },
]
