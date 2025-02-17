/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {defineStore} from "pinia";
/********************************************************************************
 * APP全局应用状态
 *
 * @author Berlin
 ********************************************************************************/
import type {WebsiteOption, MenuOption} from "@/ploutos/layouts/types";
import type {RouteRecordRaw} from "vue-router";
import {ref, type Ref} from "vue";

/**
 * 创建APP全局应用状态
 */
const useAppStore = defineStore('appStore', () => {

  /**
   * 设置菜单
   */
  function configMenu(options: MenuOption[]) {
    // @ts-ignore
    this.menus = options;

    // 展开菜单为平层
    // @ts-ignore
    expandMenu(this.menus, this.expandMenus);
  }

  /**
   * 设置网站信息
   */
  function configWebsite(websiteOption: WebsiteOption) {
    // @ts-ignore
    this.websiteOption = websiteOption;
  }

  /**
   * 展开菜单
   */
  function expandMenu(menus: MenuOption[], expandMenus: MenuOption[]) {
    menus.forEach((menu: MenuOption) => {
      expandMenus.push(menu);
      if (menu.children && menu.children.length > 0) {
        expandMenu(menu.children, expandMenus);
      }
    });
  }

  return { ...appStore, configMenu, configWebsite }
}, {
  persist: true
});
export default useAppStore;

/**
 * 初始APP全局应用状态
 */
const appStore: AppStoreOption = {
  menus: [],
  expandMenus: [],
  websiteOption: {
    title: '',
    subtitle: '',
    companyName: '',
    version: ''
  },
  visitedRoutes: ref([])
}

/**
 * APP应用状态类型定义
 */
type AppStoreOption = {

  /**
   * 菜单
   */
  menus: MenuOption[],

  /**
   * 展开的菜单
   */
  expandMenus: MenuOption[],

  /**
   * 网站信息
   */
  websiteOption: WebsiteOption,

  /**
   * 选项卡路由
   */
  visitedRoutes: Ref<RouteRecordRaw[]>
}