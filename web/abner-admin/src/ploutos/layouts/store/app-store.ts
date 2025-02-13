/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {defineStore} from "pinia";
import type {MenuOption} from 'naive-ui';
/********************************************************************************
 * APP全局应用状态
 *
 * @author Berlin
 ********************************************************************************/
import type {WebsiteOption} from "@/ploutos/layouts/types";

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
});
export default useAppStore;

/**
 * 初始APP全局应用状态
 */
const appStore = {

  /**
   * 菜单
   */
  menus: [],

  /**
   * 展开的菜单
   */
  expandMenus: [],

  /**
   * 网站信息
   */
  websiteOption: {

    /**
     * 网站标题
     */
    title: '',

    /**
     * 副标题
     */
    subtitle: '',

    /**
     * 公司名称
     */
    companyName: '',

    /**
     * 应用版本
     */
    version: ''
  },
}
