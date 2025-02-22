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
import {type Ref, ref} from "vue";

/**
 * 创建布局状态存储器
 */
const useLayoutStore = defineStore('layoutStore', () => {
  return { ...layoutStore }
}, {
  persist: {
    pick: ['theme', 'themeColor', 'sideTheme', 'layoutMode']
  },
});
export default useLayoutStore;

/**
 * 初始布局状态
 */
const layoutStore: LayoutStoreOption = {
  theme: ref(ThemeMode.LIGHT),
  themeColor: ref('#18a058'),
  sideTheme: ref(SideTheme.WHITE),
  deviceType: ref(DeviceType.PC),
  layoutMode: ref(LayoutMode.LeftRight),
  isCollapse: ref(true),
};

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
  isCollapse: Ref<boolean>
}