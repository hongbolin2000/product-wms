/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {defineStore} from "pinia";
/********************************************************************************
 * APP全局应用状态
 *
 * @author Berlin
 ********************************************************************************/
import type {MenuOption, WebsiteOption} from "@/ploutos/layouts/types";
import {ref, type Ref} from "vue";

/**
 * 创建APP全局应用状态
 */
const useAppStore = defineStore('appStore', () => {

  /**
   * 设置菜单
   */
  function configMenu(options: MenuOption[]) {
    this.menus = options;

    // 展开菜单为平层
    expandMenu(this.menus, this.expandMenus);

    // 固定的菜单选项卡
    this.expandMenus.forEach(menu => {
      const tabMenu = this.visitedMenus.find(i => i.key === menu.key);
      if (tabMenu) {
        tabMenu.fixed = menu.fixed;
      } else if (menu.fixed) {
        // 拿第一层菜单的图标
        if (!menu.icons) {
          const paths = menu.key.split("/");
          const firstMenu = this.expandMenus.findLast(i => i.key === "/" + paths[1]);
          menu.icons = firstMenu && firstMenu.icons;
        }
        this.visitedMenus.push(menu);
      }
    });
  }

  /**
   * 设置网站信息
   */
  function configWebsite(websiteOption: WebsiteOption) {
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
  visitedMenus: ref([])
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
  visitedMenus: Ref<MenuOption[]>
}