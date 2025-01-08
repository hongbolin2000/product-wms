<template>
  <n-config-provider
    :theme-overrides="themeOverrides"
    :theme="theme"
    :locale="zhCN"
    style="height: 100%"
  >
    <n-global-style />
    <n-dialog-provider>
      <n-el
        class="vaw-layout-container"
        :class="[layoutStore.deviceType === 'mobile' && 'is-mobile']"
      >
        <template v-if="layoutStore.layoutMode === 'ttb'">
          <VawHeader/>
          <MainLayout :show-nav-bar="false"/>
        </template>
        <template v-else-if="layoutStore.layoutMode === 'lcr'">
          <SplitSideBar/>
          <MainLayout/>
        </template>
        <template v-else>
          <SideBar/>
          <MainLayout />
        </template>

        <div
          v-if="layoutStore.deviceType === 'mobile'"
          class="mobile-shadow"
          :class="[layoutStore.isCollapse ? 'close-shadow' : 'show-shadow']"
          @click="closeMenu"
        ></div>
      </n-el>
    </n-dialog-provider>
  </n-config-provider>
</template>

<script setup lang="ts">
  import {computed, onBeforeUnmount, onMounted} from "vue"
  import { darkTheme, zhCN } from 'naive-ui'
  /********************************************************************************
   * 框架布局
   *
   * @author Berlin
   ********************************************************************************/
  import {DeviceType, ThemeMode} from '@/layouts/types'
  import SideBar from '@/layouts/sidebar/SideBar.vue'
  import MainLayout from "@/layouts/MainLayout.vue";
  import {useLayoutStore} from "@/layouts/store/layout-store";
  import VawHeader from "@/layouts/header/VawHeader.vue";
  import SplitSideBar from "@/layouts/sidebar/SplitSideBar.vue";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

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
  });

  /**
   * 主题
   */
  const theme = computed(() => {
    return layoutStore.theme === ThemeMode.DARK ? darkTheme : null
  });

  /**
   * 组件加载
   */
  onMounted(() => {
    onScreenResize()
    window.addEventListener('resize', onScreenResize)
  });

/**
 * 组件卸载
 */
  onBeforeUnmount(() => {
    window.removeEventListener('resize', onScreenResize)
  });

  /**
   * 屏幕大小改变
   */
  function onScreenResize() {
    const width = document.body.clientWidth
    if (width <= 768) {
      layoutStore.deviceType = DeviceType.MOBILE;
      layoutStore.isCollapse = true;
    } else if (width < 992 && width > 768) {
      layoutStore.deviceType = DeviceType.PAD;
      layoutStore.isCollapse = true;
    } else if (width < 1200 && width >= 992) {
      layoutStore.deviceType = DeviceType.PC;
      layoutStore.isCollapse = false;
    } else {
      layoutStore.deviceType = DeviceType.PC;
      layoutStore.isCollapse = false;
    }
  }

  /**
   * 关闭菜单
   */
  function closeMenu() {
    layoutStore.isCollapse = true;
  }
</script>

<style lang="scss">
  .vaw-layout-container {
    height: 100%;
    max-width: 100%;
    overflow-x: hidden;
    .mobile-shadow {
      display: none;
    }
    .layout-mode-ttb {
      margin-top: $logoHeight;
      transition: all $transitionTime;
    }
  }

  .is-mobile {
    .mobile-shadow {
      background-color: #000000;
      position: fixed;
      top: 0;
      left: 0;
      width: 100vw;
      height: 100vh;
      z-index: 997;
    }
    .close-shadow {
      display: none;
    }
    .show-shadow {
      display: block;
      opacity: 0.5;
      transition: all $transitionTime;
    }
  }
</style>
