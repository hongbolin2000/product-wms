/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {type RouteRecordRaw} from 'vue-router'
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
    path: '/gr09/add',
    component: () => import('@/views/gr/gr09/production-editor.vue')
  }
]
export default routes;
