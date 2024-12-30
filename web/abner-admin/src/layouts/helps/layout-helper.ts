/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 布局框架中的帮助类（提供给功能应用调用）
 *
 * @author Berlin
 *******************************************************************************/
import {useAppStore} from "@/layouts/store/app-store";
import {type Router, useRouter} from "vue-router";
import {exceptionRoutes} from '@/layouts/exception/exception-routes';

/**
 * 布局框架中的帮助类（提供给功能应用调用）
 *
 * @param props 传入的属性
 */
export namespace helper {

  /**
   * 初始化系统配置
   *
   * @param props 传入的属性
   */
  export function initial(props: {
    menus: [], websiteTitle: string
  }) {

    /**
     * 全局应用状态
     */
    const appStore = useAppStore();

    /**
     * 配置菜单
     */
    appStore.configMenu(props.menus);

    /**
     * 设置网站信息
     */
    appStore.configWebsite({
      title: props.websiteTitle
    })
  }

  /**
   * 加载异常路由
   */
  export function useExceptionRoute(router: Router) {

    /**
     * 将异常路由加入路由对象中
     */
    exceptionRoutes.forEach(route => {
      router.addRoute(route);
    })
  }
}
