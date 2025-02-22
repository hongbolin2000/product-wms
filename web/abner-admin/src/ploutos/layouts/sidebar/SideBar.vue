<template>
  <n-config-provider :theme="theme">
    <n-card
      class="vaw-side-bar-wrapper"
      :bordered="false"
      style="border-radius: 0;"
      :content-style="{ padding: 0 }"
      :class="[
        layoutStore.isCollapse ? 'close-status' : 'open-status',
        layoutStore.sideTheme == SideTheme.IMAGE ? 'sidebar-bg-img' : '',
      ]"
    >
      <Logo/>
      <ScrollerMenu/>
      <div class="mobile-shadow"/>
    </n-card>
  </n-config-provider>
</template>

<script setup lang="ts">
  import {computed, type Ref} from "vue";
  import {darkTheme} from "naive-ui";
  /********************************************************************************
   * 侧边栏菜单布局
   *
   * @author Berlin
   ********************************************************************************/
  import {SideTheme, ThemeMode} from '@/ploutos/layouts/types'
  import Logo from '@/ploutos/layouts/logo/Logo.vue'
  import ScrollerMenu from "@/ploutos/layouts/menus/VerticalMenu.vue";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 主题
   */
  const theme: Ref = computed(() => {
    if (layoutStore.theme == ThemeMode.DARK || layoutStore.sideTheme == SideTheme.DARK) {
      return darkTheme;
    }
    return null;
  });
</script>

<style scoped lang="scss">
  .vaw-side-bar-wrapper {
    position: fixed;
    z-index: 999;
  }
  .open-status {
    width: $menuWidth;
    transition: all $transitionTime;
  }
  .close-status {
    width: $minMenuWidth;
    transition: all $transitionTime;
  }
  .is-mobile {
    .close-status {
      $negativeMenuWidth: calc(#{$menuWidth} * -1);
      transform: translateX($negativeMenuWidth);
    }
  }
  .sidebar-bg-img {
    background-image: url('./menu-bg.webp') !important;
    background-size: cover;
  }
</style>
