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
    path: '/sy01/permission/assign/:roleId',
    component: () => import('@/views/sy/sy01/permission-assign.vue')
  },
]
export default routes;
