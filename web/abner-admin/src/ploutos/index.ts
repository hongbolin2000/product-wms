/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 布局框架
 *
 * @author Berlin
 ********************************************************************************/

/**
 * 帮助工具类
 */
import routerHelper from "@/ploutos/layouts/helps/router-helper";
import appHelper from "@/ploutos/layouts/helps/app-helper";
import cryptoHelper from "@/ploutos/layouts/helps/crypto-helper";

/**
 * 反馈组件
 */
import notification from "@/ploutos/layouts/feedback/notification";
import message from "@/ploutos/layouts/feedback/message";
import dialog from "@/ploutos/layouts/feedback/dialog";
import screen from "@/ploutos/layouts/feedback/screen";

/**
 * 网络请求
 */
import http from '@/ploutos/layouts/axios/http';

/**
 * 布局控件
 */
const ThemeLayout = import('@/ploutos/layouts/ThemeLayout.vue')

/**
 * 通用界面
 */
import Graces from '@/ploutos/graces/ag01/index';
import useLayoutStore from "@/ploutos/layouts/store/layout-store";

/**
 * 加载控件
 */
function loading(value: boolean) {
  const layoutStore = useLayoutStore();
  layoutStore.loading = value;
}

export {
  routerHelper,
  appHelper,
  cryptoHelper,
  notification,
  message,
  dialog,
  screen,
  http,
  ThemeLayout,
  Graces,
  loading,
};
