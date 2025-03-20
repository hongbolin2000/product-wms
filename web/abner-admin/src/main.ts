/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import {createPersistedState} from "pinia-plugin-persistedstate";
import {routerHelper} from '@/ploutos';
/********************************************************************************
 * Vue起始文件
 *
 * @author Berlin
 *******************************************************************************/
import App from '@/App.vue'
import {router} from '@/routes'

/**
 * 创建应用
 */
function create() {

  /**
   * 初始化路由（在路由创建前配置）
   */
  routerHelper.initRouter({
    router: router,
    rootPath: "/sy99",
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
