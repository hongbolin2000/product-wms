/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {defineStore} from "pinia";
import {ref, type Ref} from "vue";
/********************************************************************************
 * APP全局应用状态
 *
 * @author Berlin
 ********************************************************************************/
import type {MenuOption, WebsiteOption} from "@/ploutos/layouts/types";

/**
 * 创建APP全局应用状态
 */
const useAppStore = defineStore('appStore', () => {

  /**
   * 状态定义
   */
  const store: AppStoreOption = {
    menus: ref([]),
    expandMenus: ref([]),
    topLeftChildMenus: ref([]),
    websiteOption: ref({
      title: '',
      subtitle: '',
      companyName: '',
      version: ''
    }),
    visitedMenus: ref([])
  }

  /**
   * 设置菜单
   */
  function configMenu(options: MenuOption[]) {
    store.menus.value = options;

    // 展开菜单为平层
    expandMenu(store.menus.value, store.expandMenus.value);

    // 添加固定的菜单选项卡
    addFixedVisitedMenu();

    // 移除选项卡中没有的菜单(权限认证)
    removeVisitedMenu();
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

  /**
   * 添加固定的菜单选项卡
   */
  function addFixedVisitedMenu() {
    store.expandMenus.value.forEach(menu => {
      const tabMenu = store.visitedMenus.value.find(i => i.key === menu.key);
      if (tabMenu) {
        tabMenu.fixed = menu.fixed;
      } else if (menu.fixed) {
        if (!menu.icons) {
          menu.icons = menu.parentIcon;
        }
        // 将固定的菜单加入选项卡
        store.visitedMenus.value.push(menu);
      }
    });
  }

  /**
   * 移除选项卡中没有的菜单
   */
  function removeVisitedMenu() {
    [...store.visitedMenus.value].forEach(tabMenu => {
      const menu = store.expandMenus.value.find(i => i.key === tabMenu.key);
      if (!menu) {
        const visitedIndex = store.visitedMenus.value.findIndex(i => i.key === tabMenu.key);
        store.visitedMenus.value.splice(visitedIndex, 1);
      }
    });
  }

  /**
   * 设置网站信息
   */
  function configWebsite(websiteOption: WebsiteOption) {
    store.websiteOption.value = websiteOption;
  }

  return { ...store, configMenu, configWebsite }
}, {
  persist: {
    pick: ['visitedMenus']
  }
});
export default useAppStore;

/**
 * APP应用状态类型定义
 */
type AppStoreOption = {

  /**
   * 菜单
   */
  menus: Ref<MenuOption[]>,

  /**
   * 展开的菜单
   */
  expandMenus: Ref<MenuOption[]>,

  /**
   * 顶部+左侧模式子菜单
   */
  topLeftChildMenus: Ref<MenuOption[]>,

  /**
   * 网站信息
   */
  websiteOption: Ref<WebsiteOption>,

  /**
   * 选项卡路由
   */
  visitedMenus: Ref<MenuOption[]>
}