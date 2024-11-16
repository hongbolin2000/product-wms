/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import { createWebHashHistory, createRouter } from 'vue-router'
/********************************************************************************
 * 应用路由配置
 *
 * @author Berlin
 *******************************************************************************/
import Login from '@/views/Login/Login.vue';
import Main from '@/views/Main.vue';

/**
 * 路由配置
 */
const routes = [
  { path: '/', component: Main },
  { path: '/login', component: Login },
]

export const routers = createRouter({
  history: createWebHashHistory(),
  routes,
})
