<template>
  <n-config-provider :theme-overrides="themeOverThemes">
    <n-scrollbar :x-scrollable="true">
        <n-menu
          mode="horizontal"
          :value="currentPath"
          :options="appStore.menus"
          @update:value="onMenuClick"
        />
    </n-scrollbar>
  </n-config-provider>
</template>

<script setup lang="ts">
  import {computed, ref, watch} from "vue";
  import {useRoute, useRouter} from "vue-router";
  /********************************************************************************
   * 水平菜单
   *
   * @author Berlin
   ********************************************************************************/
  import useAppStore from "@/ploutos/layouts/store/app-store";
  import {SideTheme, ThemeMode} from "@/ploutos/layouts/types.ts";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store.ts";

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
  const currentRoute = useRoute();

  /**
   * 当前路由地址
   */
  const currentPath = ref('');
  currentPath.value = currentRoute.path;

  /**
   * 点击菜单
   */
  function onMenuClick(key: string) {
    router.push(key);
  }

/**
 * 重写主题样式
 */
const themeOverThemes = computed(() => {

  if (layoutStore.theme == ThemeMode.DARK || layoutStore.sideTheme == SideTheme.DARK || layoutStore.sideTheme == SideTheme.IMAGE) {
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
   * 监听路由变化
   */
  watch(() => currentRoute.path, (value) => {
    currentPath.value = value.split("?")[0];
  });
</script>

<style scoped lang="scss">
  :deep(.n-menu-item-content__icon) {
    font-size: 16px !important;
  }
  :deep(.n-menu .n-menu-item) {
    margin-top: 0;
  }
  :deep(.n-menu .n-menu-item::before) {
    left: 0;
    right: 0;
    border-radius: 3px;
    margin: 0 5px;
  }
  :deep(.n-menu-item-content-header) {
    overflow: inherit !important;
  }
</style>
