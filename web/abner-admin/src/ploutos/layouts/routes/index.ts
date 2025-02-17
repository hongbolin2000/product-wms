/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {type RouteRecordRaw} from 'vue-router'
/********************************************************************************
 * 布局框架固定路由配置
 *
 * @author Berlin
 *******************************************************************************/

/**
 * 布局框架固定路由配置
 */
export const layoutRoutes: RouteRecordRaw[] = [
  { path: '/403', name: 'Unauthorized', component: () => import('@/ploutos/layouts/exception/component/403.vue') },
  { path: '/404', name: 'NotFound', component: () => import('@/ploutos/layouts/exception/component/404.vue') },
  { path: '/login', name: 'Login', component: () => import('@/ploutos/layouts/login/Login.vue') },
  { path: '/splash', name: 'Splash', component: () => import('@/ploutos/layouts/splash/Splash.vue') },
  { path: '/:pathMatch(.*)*', name: 'redirect', redirect: '/404'}
]