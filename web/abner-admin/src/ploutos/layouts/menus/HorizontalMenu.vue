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
  import {LayoutMode, SideTheme, ThemeMode} from "@/ploutos/layouts/types";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";

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
  const currentPath = ref(route.path);

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
   * 点击菜单
   */
  function onMenuClick(key: string) {
    router.push(key);
  }

  /**
   * 监听路由变化
   */
  watch(() => route.path, (value) => {
    currentPath.value = value.split("?")[0];
  });
</script>

<style scoped lang="scss">
  :deep(.n-menu-item-content-header) {
    overflow: inherit !important;
  }
</style>
