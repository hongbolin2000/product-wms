/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {type RouteLocationNormalized, type Router} from "vue-router";
/********************************************************************************
 * Router帮助类（提供给功能应用调用）
 *
 * @author Berlin
 *******************************************************************************/
import {layoutRoutes} from '@/ploutos/layouts/routes';

/**
 * Router帮助类（提供给功能应用调用）
 *
 * @param props 传入的属性
 */
namespace routerHelper {

  /**
   * 初始化路由
   */
  export function initRouter(props: {
    router: Router, rootPath: string
  }) {

    // 配置'/'路由的默认访问地址
    if (props.rootPath) {
      props.router.addRoute({
        path: '/',
        redirect: props.rootPath,
        meta: {
          hidden: true,
        },
      })
    }

    // 加入框架路由
    layoutRoutes.forEach(route => {
      props.router.addRoute(route);
    });
  }

  /**
   * 检查理由路径是否在布局中忽略
   */
  export function isIgnoreRoute(route: RouteLocationNormalized) {
    return ['/login', '/splash', '/refresh/fresh', '/403', '/404', '/index'].includes(route.path);

  }
}
export default routerHelper;
