/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {type RouteRecordNormalized} from 'vue-router'
/********************************************************************************
 * 布局框架固定路由配置
 *
 * @author Berlin
 *******************************************************************************/

/**
 * 布局框架固定路由配置
 */
export const layoutRoutes: RouteRecordNormalized[] = [
  { path: '/403', name: 'Unauthorized', component: () => import('@/layouts/exception/component/403.vue') },
  { path: '/404', name: 'NotFound', component: () => import('@/layouts/exception/component/404.vue') },
  { path: '/500', name: 'InnerError', component: () => import('@/layouts/exception/component/500.vue') },
  { path: '/login', name: 'Login', component: () => import('@/layouts/login/Login.vue') },
  { path: '/splash', name: 'Splash', component: () => import('@/layouts/splash/Splash.vue') },
  { path: '/:pathMatch(.*)*', name: 'redirect', redirect: '/404'}
]