/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import { createApp } from 'vue'
import { createPinia } from 'pinia'
/********************************************************************************
 * Vue起始文件
 *
 * @author Berlin
 *******************************************************************************/
import '@/styles'
import App from '@/App.vue'
import {routers} from '@/router'

const app = createApp(App)
app.use(routers)
app.use(createPinia());
app.mount('#app')
