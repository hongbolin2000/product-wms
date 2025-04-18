/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 应用系统帮助类（提供给功能应用调用）
 *
 * @author Berlin
 *******************************************************************************/
import useAppStore from "@/ploutos/layouts/store/app-store";
import useLayoutStore from "@/ploutos/layouts/store/layout-store";
import type {MenuOption} from "@/ploutos/layouts/types";

/**
 * 应用系统的帮助类（提供给功能应用调用）
 */
namespace appHelper {

  /**
   * 设置平台版本号
   *
   * @param version 系统版本号
   */
  export function version(version: string) {

    /**
     * 全局应用状态
     */
    const appStore = useAppStore();

    /**
     * 设置平台信息
     */
    appStore.version = version;
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

  /**
   * 关闭当前选项卡
   */
  export async function closeCurrentTab() {

    /**
     * 全局应用状态
     */
    const appStore = useAppStore();

    /**
     * 关闭当前选项卡
     */
    await appStore.closeCurrentTab();
  }

  /**
   * 更改选项卡标题
   */
  export function changeTabTitle(title: string) {

    /**
     * 全局应用状态
     */
    const appStore = useAppStore();

    /**
     * 更改选项卡标题
     */
    appStore.changeTabTitle(title);
  }

  /**
   * 计算主内容最大高度
   */
  export const maxHeight = () => {
    const layoutStore = useLayoutStore();
    if (layoutStore.isFullScreen) {
      return '100vh - 10px';
    }
    return '100vh - var(--logo-height) - var(--tab-height)'
  }
}
export default appHelper;
