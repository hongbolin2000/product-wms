/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {createWebHashHistory, createRouter, type RouteRecordNormalized} from 'vue-router'
/********************************************************************************
 * 应用路由配置
 *
 * @author Berlin
 *******************************************************************************/

/**
 * 路由配置
 */
export const routes: RouteRecordNormalized = [
  {
    path: '/index',
    component: () => import('@/layouts/ThemeLayout.vue'),
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
    component: () => import('@/layouts/ThemeLayout.vue'),
    children: [
      {
        path: '/system/index',
        component: () => import("@/views/index/work-space.vue"),
      }
    ]
  },
]

export const router = createRouter({
  history: createWebHashHistory(),
  routes,
})
