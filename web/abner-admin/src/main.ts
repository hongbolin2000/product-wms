/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import { createApp } from 'vue'
/********************************************************************************
 * Vue起始文件
 *
 * @author Berlin
 *******************************************************************************/
import App from './App.vue'
import {routers} from '@/router';

const app = createApp(App)
app.use(routers);
app.mount('#app')
