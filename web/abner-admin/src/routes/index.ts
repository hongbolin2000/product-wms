/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {createWebHashHistory, createRouter, type Router, type RouteRecordNormalized} from 'vue-router'
import {ThemeLayout} from '@/ploutos';
/********************************************************************************
 * 应用路由配置
 *
 * @author Berlin
 *******************************************************************************/

/**
 * 路由配置
 */
const routes: RouteRecordNormalized = [
  {
    path: '/index',
    component: () => ThemeLayout,
    children: [
      {
        path: '/index/home',
        component: () => import('@/views/index/main-control.vue'),
      },
      {
        path: '/index/work-place',
        component: () => import("@/views/index/work-space.vue"),
      },
    ]
  },
  {
    path: '/system',
    component: () => ThemeLayout,
    children: [
      {
        path: '/system/index',
        component: () => import("@/views/index/work-space.vue"),
      }
    ]
  },
]

/**
 * 创建路由对象
 */
export const router: Router = createRouter({
  history: createWebHashHistory(),
  routes: routes,
});
