/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import { defineStore } from 'pinia'
/********************************************************************************
 * 布局状态
 *
 * @author Berlin
 ********************************************************************************/
import {DeviceType, LayoutMode, SideTheme, ThemeMode} from "@/ploutos/layouts/types";
import {type Ref, shallowRef} from "vue";
import {screen} from "@/ploutos";

/**
 * 创建布局状态存储器
 */
const useLayoutStore = defineStore('layoutStore', () => {

  /**
   * 布局状态
   */
  const layoutStore: LayoutStoreOption = {
    theme: shallowRef(ThemeMode.LIGHT),
    themeColor: shallowRef('#ee4f12'),
    themeBgColor: shallowRef('#f0f2f5'),
    sideTheme: shallowRef(SideTheme.WHITE),
    deviceType: shallowRef(DeviceType.PC),
    layoutMode: shallowRef(LayoutMode.LeftRight),
    isCollapse: shallowRef(true),
    bordered: shallowRef(false),
    striped: shallowRef(false),
    loading: shallowRef(false),
    isFullScreen: shallowRef(false),
  };

  /**
   * 内容全屏
   */
  async function fullScreen() {
    const clazz = layoutStore.theme.value == ThemeMode.DARK ? 'page-full-screen-dark' : 'page-full-screen-light';
    await screen.fullElement('layout-main-content', [clazz]);
    layoutStore.isFullScreen.value = true;
  }

  return { ...layoutStore, fullScreen }
}, {
  persist: {
    pick: ['theme', 'themeColor', 'themeBgColor', 'sideTheme', 'layoutMode', 'bordered', 'striped']
  },
});
export default useLayoutStore;

/**
 * 布局状态类型定义
 */
type LayoutStoreOption = {

  /**
   * 主题
   */
  theme: Ref<ThemeMode | undefined>;

  /**
   * 主题颜色
   */
  themeColor: Ref<string>,

  /**
   * 主题背景色
   */
  themeBgColor: Ref<string>,

  /**
   * 菜单栏主题
   */
  sideTheme?: Ref<SideTheme | undefined>,

  /**
   * 终端
   */
  deviceType: Ref<DeviceType>,

  /**
   * 布局模式
   */
  layoutMode?: Ref<LayoutMode | undefined>,

  /**
   * 是否折叠菜单
   */
  isCollapse: Ref<boolean>,

  /**
   * 是否显示表格边框
   */
  bordered: Ref<boolean>,

  /**
   * 是否显示表格斑马格
   */
  striped: Ref<boolean>,

  /**
   * loading
   */
  loading: Ref<boolean>,

  /**
   * 是否全屏
   */
  isFullScreen: Ref<boolean>;
}