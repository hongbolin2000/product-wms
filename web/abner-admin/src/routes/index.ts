/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {createWebHashHistory, createRouter, type Router, type RouteRecordRaw} from 'vue-router'
import {ThemeLayout} from '@/ploutos';
/********************************************************************************
 * 应用路由配置
 *
 * @author Berlin
 *******************************************************************************/
import SY01 from '@/views/sy/sy01/routes.ts';
import SY02 from '@/views/sy/sy02/routes.ts';

/**
 * 路由配置
 */
const index: RouteRecordRaw[] = [
  {
    path: '/index',
    component: () => ThemeLayout,
    children: [
      ...SY01,
      ...SY02
    ]
  }
]

/**
 * 创建路由对象
 */
export const router: Router = createRouter({
  history: createWebHashHistory(),
  routes: index,
});
