<template>
  <n-config-provider :theme-overrides="themeOverThemes">
    <n-scrollbar :x-scrollable="true">
        <n-menu
          mode="horizontal"
          :value="currentPath"
          :options="parentMenus"
          @update:value="onMenuClick"
        />
    </n-scrollbar>
  </n-config-provider>
</template>

<script setup lang="ts">
  import {computed, onMounted, ref, type Ref, watch} from "vue";
  import {useRoute, useRouter} from "vue-router";
  import {storeToRefs} from 'pinia';
  /********************************************************************************
   * 水平菜单
   *
   * @author Berlin
   ********************************************************************************/
  import useAppStore from "@/ploutos/layouts/store/app-store";
  import {LayoutMode, ThemeMode} from "@/ploutos/layouts/types";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import type {MenuOption} from "naive-ui";

  /**
   * 全局应用状态
   */
  const appStore = useAppStore();
  const layoutStore = useLayoutStore();

  /**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 当前路由
   */
  const route = useRoute();

  /**
   * 当前路由地址
   */
  const currentPath = ref('');

  /**
   * 一级菜单
   */
  const parentMenus: Ref<MenuOption[]> = ref([]);

  /**
   * 组件加载
   */
  onMounted(() => {
    if (appStore.menus.length > 0) {
      renderMenus();
    }
  });

  /**
   * 菜单加载
   */
  const { menus } = storeToRefs(appStore);
  watch(menus, () => {
    renderMenus();
  });

  /**
   * 生成菜单
   */
  function renderMenus() {
    const topMenus = [];

    // 顶部+左侧菜单模式
    if (layoutStore.layoutMode == LayoutMode.TopLeft) {
      appStore.menus.forEach(menu => {
        const topMenu = {...menu}
        delete topMenu.children;
        topMenus.push(topMenu);
      });
      parentMenus.value = topMenus;

      // 当前激活的菜单
      currentPath.value = "/" + route.fullPath.split("/")[1];
      matchChildMenus(currentPath.value);
    }

    // 其他模式
    if (layoutStore.layoutMode != LayoutMode.TopLeft) {
      parentMenus.value = appStore.menus;
      currentPath.value = route.fullPath;
    }
  }

  /**
   * 重写主题样式
   */
  const themeOverThemes = computed(() => {

    // 暗黑模式
    if (layoutStore.theme == ThemeMode.DARK) {
      return {
        common: {
          textColor1: '#bbbbbb', // 图标
          textColor2: '#bbbbbb', // 文字
          hoverColor: 'none', // 鼠标经过背景
        },
        Menu: {
          // 父菜单
          itemTextColorHoverHorizontal: '#fff', // 鼠标经过文字颜色
          itemIconColorHoverHorizontal: '#fff', // 鼠标经过图标颜色
          itemTextColorChildActiveHorizontal: '#fff', // 激活时文字颜色
          itemIconColorChildActiveHorizontal: '#fff', // 激活时文字颜色
          itemTextColorChildActiveHoverHorizontal: '#fff', // 激活时鼠标经过文字颜色
          itemIconColorChildActiveHoverHorizontal: '#fff', // 激活时鼠标经过图标颜色

          // 没有children的菜单
          itemTextColorActiveHorizontal: '#fff', // 激活时文字颜色
          itemIconColorActiveHorizontal: '#fff', // 激活时图标颜色
          itemTextColorActiveHoverHorizontal: '#fff', // 激活时鼠标经过文字颜色
          itemIconColorActiveHoverHorizontal: '#fff', // 激活时鼠标经过图标颜色
        },
        Dropdown: {
          // 子菜单
          optionTextColorHover: '#fff', // 鼠标经过文字颜色
          optionTextColorActive: '#fff', // 激活时文字颜色
          optionTextColorChildActive: '#fff', // 激活时子菜单的父菜单文字颜色
          optionColorActive: layoutStore.themeColor // 激活时背景颜色
        }
      }
    }
    return {}
  });

  /**
   * 匹配子菜单(顶部+左侧菜单模式)
   */
  function matchChildMenus(key: string) {
    const parentMenu = appStore.expandMenus.find(i => i.key == key);
    if (!parentMenu) {
      appStore.topLeftChildMenus = appStore.menus[0].children;
      currentPath.value = '/' + appStore.menus[0].fullUrl.split("/")[1];
      return;
    }

    // 没有子菜单
    let topLeftChildMenus = [];
    if (!parentMenu?.children) {
      topLeftChildMenus.push(parentMenu);
    } else{
      topLeftChildMenus = parentMenu.children;
    }
    appStore.topLeftChildMenus = topLeftChildMenus;
  }

  /**
   * 点击菜单
   */
  function onMenuClick(key: string) {
    if (layoutStore.layoutMode != LayoutMode.TopLeft) {
      router.push(key);
    }

    // 顶部+左侧菜单模式
    if (layoutStore.layoutMode == LayoutMode.TopLeft) {
      matchChildMenus(key);

      // 路由到第一个菜单
      for (let i = 0; i < appStore.topLeftChildMenus.length; i++) {
        if (!appStore.topLeftChildMenus[i].children) {
          router.push(appStore.topLeftChildMenus[i].key);
          break;
        }
      }
    }
  }

  /**
   * 监听路由变化
   */
  watch(() => route.fullPath, (value) => {
    const menu = appStore.expandMenus.find(i => i.key == value);

    // 顶部+左侧菜单模式
    if (layoutStore.layoutMode == LayoutMode.TopLeft) {
      currentPath.value = '/' + menu.fullUrl.split("/")[1];
    } else {
      currentPath.value = value;
    }
  });

  /**
   * 侦听布局变化
   */
  const { layoutMode } = storeToRefs(layoutStore);
  watch(layoutMode, () => {
    renderMenus();
  });
</script>

<style scoped lang="scss">
  :deep(.n-menu-item-content-header) {
    overflow: inherit !important;
  }
</style>
