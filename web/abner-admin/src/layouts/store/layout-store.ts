/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import { defineStore } from 'pinia'
/********************************************************************************
 * 布局状态
 *
 * @author Berlin
 ********************************************************************************/
import {DeviceType, SideTheme, ThemeMode} from "@/layouts/types";
import {ref} from "vue";

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
const layoutStore = {

  /**
   * 主题
   */
  theme: ref(ThemeMode.LIGHT),

  /**
   * 主题颜色
   */
  themeColor: ref('#18a058'),

  /**
   * 菜单栏主题
   */
  sideTheme: ref(SideTheme.WHITE),

  /**
   * 终端
   */
  deviceType: ref(DeviceType.PC),

  /**
   * 布局模式
   */
  layoutMode: ref(''),

  /**
   * 菜单宽度
   */
  sideWidth: ref(210),

  /**
   * 是否折叠菜单
   */
  isCollapse: ref(false),

  /**
   * 是否固定导航栏
   */
  isFixedNavBar: ref(true),

  /**
   * 页面切换效果
   */
  pageAnimate: ref('opacity'),

  /**
   * 导航栏配置
   */
  navbar: ref({

    /**
     * 是否显示搜索栏
     */
    isShowSearch: true,

    /**
     * 是否显示消息通知
     */
    isShowMessage: false,

    /**
     * 是否显示刷新图标
     */
    isShowRefresh: true,

    /**
     * 是否显示全屏图标
     */
    isShowFullScreen: true,
  })
}
