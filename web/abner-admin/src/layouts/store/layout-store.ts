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

/**
 * 创建布局状态存储器
 */
export const useLayoutStore = defineStore('layoutStore', () => {
  return { ...appConfig }
});

/**
 * 初始布局状态
 */
const appConfig = {

  /**
   * 主题
   */
  theme: ThemeMode.LIGHT,

  /**
   * 主题颜色
   */
  themeColor: '#18a058',

  /**
   * 菜单栏主题
   */
  sideTheme: SideTheme.WHITE,

  /**
   * 终端
   */
  deviceType: DeviceType.PC,

  /**
   * 布局模式
   */
  layoutMode: '',

  /**
   * 菜单宽度
   */
  sideWidth: 210,

  /**
   * 是否折叠菜单
   */
  isCollapse: false,

  /**
   * 是否固定导航栏
   */
  isFixedNavBar: true,

  /**
   * 页面切换效果
   */
  pageAnimate: 'opacity',

  /**
   * 导航栏配置
   */
  navbar: {

    /**
     * 是否显示搜索栏
     */
    isShowSearch: true,

    /**
     * 是否显示消息通知
     */
    isShowMessage: true,

    /**
     * 是否显示刷新图标
     */
    isShowRefresh: true,

    /**
     * 是否显示全屏图标
     */
    isShowFullScreen: true,
  }
}
