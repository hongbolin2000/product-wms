<template>
  <div class="scrollbar">
    <n-scrollbar>
      <n-menu
        mode="vertical"
        :value="currentPath"
        :collapsed="layoutStore.isCollapse"
        :options="!menus ? appStore.menus : menus"
        :expanded-keys="expandKeys"
        :collapsed-icon-size="22"
        :collapsed-width="63"
        :indent="15"
        accordion
        @update:value="onMenuClick"
        @update:expanded-keys="onMenuExpanded"
      />
    </n-scrollbar>
  </div>
</template>

<script setup lang="ts">
  import {ref, watch, watchEffect} from "vue"
  import {useRoute, useRouter} from "vue-router"
  import {type MenuOption} from "naive-ui"
  /********************************************************************************
   * 竖向布局菜单
   *
   * @author Berlin
   ********************************************************************************/
  import {useLayoutStore} from "@/layouts/store/layout-store";
  import {useAppStore} from '@/layouts/store/app-store';
  import {DeviceType} from "@/layouts/types";

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
  const currentPath = ref(currentRoute.fullPath.split("?")[0]);

  /**
   * 展开的菜单
   */
  const expandKeys = ref(['']);

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
  watch(() => currentRoute.fullPath, (value) => {
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
