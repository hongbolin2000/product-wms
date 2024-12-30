/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {MenuOption} from "naive-ui";
import {h} from "vue";
import {NIcon} from "naive-ui";
import MyIcon from "@/layouts/icons/SvgIcon.vue";
/********************************************************************************
 * 测试菜单定义
 *
 * @author Berlin
 ********************************************************************************/

/**
 * 菜单图标
 *
 * @param icon
 */
function renderIcon(icon: string) {
  icon = !icon ? 'menu' : icon;
  return () => h(NIcon, null, { default: () => h(MyIcon, {name: icon}) })
}

/**
 * 菜单定义
 */
export const menus: MenuOption[] = [
  {
    label: 'Dashboard',
    key: '/index',
    icon: renderIcon("dashboard"),
    icon1: "dashboard",
    children: [{
      label: '主控台',
      key: '/index/home',
      icon: renderIcon("")
    },{
      label: '工作台',
      key: '/index/work-place',
      icon: renderIcon(""),
    }]
  },
  {
    label: '系统管理',
    key: '/system',
    icon: renderIcon("setting"),
    icon1: "setting",
    children: [{
      label: '系统管理1',
      key: '/system/index',
      icon: renderIcon(""),
    },{
      label: '系统管理2',
      key: '/system/index2',
      icon: renderIcon(""),
      children: [
        {
          label: '系统管理2.2',
          key: '/system/index2/index',
          icon: renderIcon(""),
        }
      ]
    }]
  },
  {
    label: '列表页面',
    key: '/list',
    icon: renderIcon("detail"),
    icon1: "detail",
    children: [],
  },
  {
    label: '表单页面',
    key: '/form',
    icon: renderIcon("file-text"),
    icon1: "file-text",
    children: [],
  },
  {
    label: '功能/组件',
    key: '/function',
    icon: renderIcon("appstore"),
    icon1: "appstore",
    children: [],
  },
  {
    label: '结果页面',
    key: '/result',
    icon: renderIcon("file-unknown"),
    icon1: "file-unknown",
    children: [],
  },
  {
    label: '编辑器',
    key: '/editor',
    icon: renderIcon("edit"),
    icon1: "edit",
    children: [],
  },
  {
    label: '拖拽',
    key: '/tuozhai',
    icon: renderIcon("interation"),
    icon1: "interation",
    children: [],
  },
  {
    label: '多级菜单',
    key: '/multiply',
    icon: renderIcon("Partition"),
    icon1: "Partition",
    children: [],
  },
  {
    label: '地图',
    key: '/map',
    icon: renderIcon("location"),
    icon1: "location",
    children: [],
  },
  {
    label: '项目依赖',
    key: '/dependence',
    icon: renderIcon("menu"),
    icon1: "menu",
    children: [],
  }
];
