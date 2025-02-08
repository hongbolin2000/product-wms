<template>
  <n-scrollbar :x-scrollable="true">
    <n-menu
      mode="horizontal"
      :value="currentPath"
      :options="appStore.menus"
      @update:value="onMenuClick"
    />
  </n-scrollbar>
</template>

<script setup lang="ts">
  import {ref, watch} from "vue";
  import {useRoute, useRouter} from "vue-router";
  /********************************************************************************
   * 水平菜单
   *
   * @author Berlin
   ********************************************************************************/
  import useAppStore from "@/layouts/store/app-store";

  /**
   * 全局应用状态
   */
  const appStore = useAppStore();

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
  currentPath.value = currentRoute.fullPath.split("?")[0];

  /**
   * 点击菜单
   */
  function onMenuClick(key: string) {
    router.push(key);
  }

  /**
   * 监听路由变化
   */
  watch(() => currentRoute.fullPath, (value) => {
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
