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
    path: '/sy99',
    component: () => import('@/views/sy/sy99/grider.vue'),
  },
  {
    path: "/sy99/editor",
    component: () => import('@/views/sy/sy99/editor.vue'),
  },
  {
    path: "/sy99/editor/:id",
    component: () => import('@/views/sy/sy99/editor.vue'),
  }
]
export default routes;
