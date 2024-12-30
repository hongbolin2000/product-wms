/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 路由防护层
 *
 * @author Berlin
 *******************************************************************************/
import {router} from "@/router/routes";
import {helper} from "@/layouts/helps/layout-helper";

export function useRouterGuard(props: {
  rootPath: string
}) {

  // 配置'/'路由的默认访问地址
  if (props.rootPath) {
    router.addRoute({
      path: '/',
      redirect: props.rootPath,
      meta: {
        hidden: true,
      },
    })
  }

  // 配置异常路由
  helper.useExceptionRoute(router);
}
