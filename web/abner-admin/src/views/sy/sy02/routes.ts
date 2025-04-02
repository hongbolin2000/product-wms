/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {type RouteRecordRaw} from 'vue-router'
import {ThemeLayout} from '@/ploutos';
/********************************************************************************
 * 功能路由
 *
 * @author Berlin
 *******************************************************************************/

/**
 * 路由配置
 */
const routes: RouteRecordRaw[] = [
  {
    path: '/sy02/add',
    component: () => import('@/views/sy/sy02/user-editor.vue')
  },
  {
    path: '/sy02/edit/:id',
    component: () => import('@/views/sy/sy02/user-editor.vue')
  },
  {
    path: '/sy02/reset/:id',
    component: () => import('@/views/sy/sy02/reset-pwd.vue')
  },
]
export default routes;
