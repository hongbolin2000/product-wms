<template>
  <n-config-provider :theme-overrides="themeOverrides" :theme="theme">
    <Login/>
  </n-config-provider>
</template>

<script setup lang="ts">
  import {computed} from "vue";
  import { darkTheme } from 'naive-ui'
  /********************************************************************************
   * 登录界面
   *
   * @author Berlin
   ********************************************************************************/
  import {useLayoutStore} from "@/layouts/store/layout-store";
  import {ThemeMode} from "@/layouts/types";
  import Login from "@/layouts/login/Login.vue";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 主题
   */
  const theme = computed(() => {
    return layoutStore.theme === ThemeMode.DARK ? darkTheme : null
  });

  /**
   * 重写主题样式
   */
  const themeOverrides = computed(() => {
    return {
      common: {
        primaryColor: layoutStore.themeColor,
        primaryColorHover: layoutStore.themeColor,
      },
    }
  })
</script>

<style scoped lang="scss">
</style>
