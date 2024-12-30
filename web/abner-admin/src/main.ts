/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import {createPersistedState} from "pinia-plugin-persistedstate";
/********************************************************************************
 * Vue起始文件
 *
 * @author Berlin
 *******************************************************************************/
import '@/layouts/styles'
import App from '@/App.vue'
import {router} from '@/router/routes'
import {useRouterGuard} from "@/router/guard";

/**
 * 创建应用
 */
function create() {

  /**
   * 路由防护（在router开始前配置）
   */
  useRouterGuard({
    rootPath: "/index/home"
  });

  // 创建pinia
  const pinia = createPinia();
  pinia.use(createPersistedState());

  // 创建应用
  const app = createApp(App);
  app.use(router);
  app.use(pinia);
  app.mount('#app');
}
create();
