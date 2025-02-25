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

/**
 * 路由配置
 */
const routes: RouteRecordRaw[] = [
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
      },
      {
        path: '/system/index3',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index4',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index5',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index6',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index7',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index8',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index9',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index10',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index11',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index12',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index13',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index14',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index15',
        component: () => import("@/views/index/work-space.vue"),
      },
      {
        path: '/system/index2',
        children: [
          {
            path: '/system/index2/index',
            component: () => import("@/views/index/work-space.vue"),
          }
        ]
      }
    ]
  }, {
    path: '/list',
    component: ThemeLayout,
    children: [
      {
        path: "/list/sample",
        component: () => import('@/ploutos/graces/grid-viewer/index.vue')
      }
    ]
  }, {
    path:'/function',
    component: ThemeLayout
  }
]

/**
 * 创建路由对象
 */
export const router: Router = createRouter({
  history: createWebHashHistory(),
  routes: routes,
});
