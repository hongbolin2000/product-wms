/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import { defineStore } from 'pinia'
/********************************************************************************
 * 布局状态
 *
 * @author Berlin
 ********************************************************************************/
import {DeviceType, SideTheme, ThemeMode} from "@/ploutos/layouts/types";
import {type Ref, ref} from "vue";

/**
 * 创建布局状态存储器
 */
const useLayoutStore = defineStore('layoutStore', () => {
  return { ...layoutStore }
}, {
  persist: true
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
  layoutMode: ref(''),
  sideWidth: ref(210),
  isCollapse: ref(false),
  isFixedNavBar: ref(true),
  pageAnimate: ref('opacity'),
  navbar: ref({
    isShowSearch: true,
    isShowMessage: false,
    isShowRefresh: true,
    isShowFullScreen: true,
  })
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
  sideTheme: Ref<SideTheme | undefined>,

  /**
   * 终端
   */
  deviceType: Ref<DeviceType>,

  /**
   * 布局模式
   */
  layoutMode: Ref<string | undefined>,

  /**
   * 菜单宽度
   */
  sideWidth: Ref<number>,

  /**
   * 是否折叠菜单
   */
  isCollapse: Ref<boolean>,

  /**
   * 是否固定导航栏
   */
  isFixedNavBar: Ref<boolean>,

  /**
   * 页面切换效果
   */
  pageAnimate: Ref<string>,

  /**
   * 导航栏属性
   */
  navbar: Ref<LayoutNavbarOption>
}

/**
 * 导航栏属性定义
 */
type LayoutNavbarOption = {

  /**
   * 是否显示搜索栏
   */
  isShowSearch: boolean,

  /**
   * 是否显示消息通知
   */
  isShowMessage: boolean,

  /**
   * 是否显示刷新图标
   */
  isShowRefresh: boolean,

  /**
   * 是否显示全屏图标
   */
  isShowFullScreen: boolean,
}