/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {defineStore} from "pinia";
import {ref, type Ref} from "vue";
import {useRoute, useRouter} from "vue-router";
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

  // 路由对象
  const route = useRoute();
  const router = useRouter();

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
   * 关闭当前页面
   */
  async function closeCurrentTab() {
    // 移除当前选项卡并跳转到最后一个选项卡
    store.visitedMenus.value.splice(getCurrentTabIndex(), 1);
    await router.push({
      path: store.visitedMenus.value[store.visitedMenus.value.length -1].key
    });
  }

  /**
   * 更改选项卡title
   */
  function changeTabTitle(title: string) {
    store.visitedMenus.value[getCurrentTabIndex()].label = title;
  }

  /**
   * 获取当前选型卡索引
   */
  function getCurrentTabIndex() {
    return store.visitedMenus.value.findIndex(i => i.key == route.path);
  }

  /**
   * 设置网站信息
   */
  function configWebsite(websiteOption: WebsiteOption) {
    store.websiteOption.value = websiteOption;
  }

  return { ...store, configMenu, configWebsite, closeCurrentTab, changeTabTitle }
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