/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * Layout布局框架中的帮助类（提供给功能应用调用）
 *
 * @author Berlin
 *******************************************************************************/
import useAppStore from "@/ploutos/layouts/store/app-store";
import type {WebsiteOption} from "@/ploutos/layouts/types";
import type {MenuOption} from "naive-ui";

/**
 * 布局框架中的帮助类（提供给功能应用调用）
 *
 * @param props 传入的属性
 */
namespace layoutHelper {

  /**
   * 初始化网站信息
   *
   * @param props 传入的属性
   */
  export function initialWebsite(props: WebsiteOption) {

    /**
     * 全局应用状态
     */
    const appStore = useAppStore();

    /**
     * 设置网站信息
     */
    appStore.configWebsite(props)
  }

  /**
   * 初始化菜单
   */
  export function initialMenu(menus: MenuOption[]) {

    /**
     * 全局应用状态
     */
    const appStore = useAppStore();

    /**
     * 设置菜单
     */
    appStore.configMenu(menus);
  }
}
export default layoutHelper;
