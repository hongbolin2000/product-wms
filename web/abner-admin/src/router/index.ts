/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {createWebHashHistory, createRouter, type RouteRecordNormalized} from 'vue-router'
/********************************************************************************
 * 应用路由配置
 *
 * @author Berlin
 *******************************************************************************/
import Login from '@/views/Login/Login.vue'
import Layout from '@/layouts/ThemeLayout.vue'
import MainControl from "@/views/index/MainControl.vue";
import WorkSpace from "@/views/index/WorkSpace.vue";

/**
 * 路由配置
 */
const routes: RouteRecordNormalized = [
  {
    path: '/index',
    component: Layout,
    meta: {
      title: 'Dashboard'
    },
    children: [
      {
        path: '/index/home',
        component: MainControl,
        meta: {
          title: '主控台'
        },
      },
      {
        path: '/index/work-place',
        component: WorkSpace,
        meta: {
          title: '工作台'
        },
      },
    ]
  },
  { path: '/login', component: Login },
]

export const routers = createRouter({
  history: createWebHashHistory(),
  routes,
})
