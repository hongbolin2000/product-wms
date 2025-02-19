<template>
  <n-config-provider :theme-overrides="themeOverThemes">
  <div class="scrollbar">
    <n-scrollbar>
      <n-menu
        mode="vertical"
        :value="currentPath"
        :collapsed="layoutStore.isCollapse"
        :options="!menus ? appStore.menus : menus"
        :expanded-keys="expandKeys"
        :collapsed-icon-size="16"
        :collapsed-width="63"
        :indent="21"
        accordion
        @update:value="onMenuClick"
        @update:expanded-keys="onMenuExpanded"
      />
    </n-scrollbar>
  </div>
  </n-config-provider>
</template>

<script setup lang="ts">
import {computed, ref, watch, watchEffect} from "vue"
import {useRoute, useRouter} from "vue-router"
/********************************************************************************
 * 竖向布局菜单
 *
 * @author Berlin
 ********************************************************************************/
import useLayoutStore from "@/ploutos/layouts/store/layout-store";
import useAppStore from '@/ploutos/layouts/store/app-store';
import {DeviceType, MenuOption, SideTheme, ThemeMode} from "@/ploutos/layouts/types";
import {darkTheme} from "naive-ui";

/**
   * 父组件传入的属性
   */
  defineProps({

    /**
     * 菜单
     */
    menus: {
      type: Array<MenuOption>,
      required: false
    }
  });

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 全局应用状态
   */
  const appStore = useAppStore();

  /**
   * 当前路由
   */
  const currentRoute = useRoute();

  /**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 当前路由地址
   */
  const currentPath = ref(currentRoute.path);

  /**
   * 展开的菜单
   */
  const expandKeys = ref(['']);

/**
 * 主题
 */
const theme = computed(() => {
  if (layoutStore.theme == ThemeMode.DARK || layoutStore.sideTheme == SideTheme.DARK) {
    return darkTheme;
  }
  return null;
});

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
        itemTextColorChildActive: '#fff', // 激活时文字颜色
        itemIconColorChildActive: '#fff', // 激活时图标颜色
        arrowColorChildActive: '#fff', // 激活时箭头颜色
        itemTextColorChildActiveHover: '#fff', // 激活时鼠标经过文字颜色
        itemIconColorChildActiveHover: '#fff', // 激活时鼠标经过图标颜色
        arrowColorChildActiveHover: '#fff', // 激活时鼠标经过箭头颜色

        // 子菜单
        itemTextColorActive: '#fff', // 激活时文字高亮颜色
        itemIconColorActive: '#fff', // 激活时图标高亮颜色
        itemTextColorHover: '#fff', // 鼠标经过文字高亮颜色
        itemIconColorHover: '#fff', // 鼠标经过图标高亮颜色
        itemColorActiveHover: layoutStore.themeColor, // 激活时鼠标经过背景颜色
        itemTextColorActiveHover: '#fff', // 激活时鼠标经过文字颜色
        itemIconColorActiveHover: '#fff', // 激活时鼠标经过图标颜色

        itemColorActive: layoutStore.themeColor, // 激活颜色
      }
    }
  }
  return {}
});

  /**
   * 加载
   */
  watchEffect(() => {
    handleExpandPath();
  });

  /**
   * 计算展开的菜单
   */
  function handleExpandPath() {

    // 分隔出每一级路由菜单
    const paths = currentPath.value.split('/');
    let expands: string[] = paths.filter((item) => !!item);

    // 将每一级路由菜单进行拼接
    expands = expands.reduce((prev: string[], current: string) => {
      const lastItem = prev[prev.length - 1]
      if (!lastItem) {
        prev.push('/' + current)
      } else {
        prev.push(lastItem + '/' + current)
      }
      return prev
    }, [] as string[]);
    expandKeys.value = expands;
  }

  /**
   * 展开菜单
   */
  function onMenuExpanded(keys: string[]) {
    expandKeys.value = keys;
  }

  /**
   * 点击菜单
   */
  function onMenuClick(key: string) {
    router.push(key);
    if (layoutStore.deviceType === DeviceType.MOBILE) {
      layoutStore.isCollapse = true;
    }
  }

  /**
   * 监听路由变化
   */
  watch(() => currentRoute.path, (value) => {
    currentPath.value = value.split("?")[0];
    handleExpandPath();
  });
</script>

<style scoped lang="scss">
  :deep(.n-menu .n-submenu .n-menu-item-content__icon) {
    font-size: 16px !important;
  }
  :deep(.n-menu .n-menu-item-content .n-menu-item-content__icon) {
    font-size: 16px !important;
  }
  :deep(.n-menu .n-menu-item) {
    margin-top: 0;
    margin-bottom: 5px;
  }
  :deep(.n-menu .n-menu-item::before) {
    left: 5px;
    right: 5px;
    border-radius: 5px;
  }
  :deep(.n-menu .n-menu-item:hover) {
    background-color: var(--item-color-active);
  }
  .scrollbar {
    height: calc(100vh - #{$logoHeight}) !important;
  }
</style>
