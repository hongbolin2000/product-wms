/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 布局框架
 *
 * @author Berlin
 ********************************************************************************/
import routerHelper from "@/layouts/helps/router-helper";
import layoutHelper from "@/layouts/helps/layout-helper";
import message from "@/layouts/feedback/message";
import dialog from "@/layouts/feedback/dialog";
import http from '@/layouts/axios/http';

/**
 * 布局控件
 */
const ThemeLayout = import('@/layouts/ThemeLayout.vue')

export {
  routerHelper, layoutHelper, message, dialog, http, ThemeLayout
};
