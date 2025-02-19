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
import layoutHelper from "@/ploutos/layouts/helps/layout-helper";

/**
 * 反馈组件
 */
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

export {
  routerHelper, layoutHelper, message, dialog, screen, http, ThemeLayout
};
