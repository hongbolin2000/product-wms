<template>
  <n-config-provider :theme="theme">
    <n-card
      class="vaw-side-bar-wrapper"
      :content-style="{ padding: 0 }"
      :bordered="layoutStore.sideTheme != SideTheme.IMAGE"
      style="border-radius: 0;"
      :class="[
        layoutStore.isCollapse ? 'close-status' : 'open-status',
        layoutStore.sideTheme == SideTheme.IMAGE ? 'sidebar-bg-img' : '',
      ]"
    >
      <Logo/>
      <VerticalMenu :vertical-menus="sideMenus"/>
      <div class="mobile-shadow"/>
    </n-card>
  </n-config-provider>
</template>

<script setup lang="ts">
  import {computed, onMounted, type Ref, ref, watch} from "vue";
  import {darkTheme} from "naive-ui";
  import {storeToRefs} from 'pinia';
  /********************************************************************************
   * 侧边栏菜单布局
   *
   * @author Berlin
   ********************************************************************************/
  import {LayoutMode, type MenuOption, SideTheme, ThemeMode} from '@/ploutos/layouts/types'
  import Logo from '@/ploutos/layouts/logo/Logo.vue'
  import VerticalMenu from "@/ploutos/layouts/menus/VerticalMenu.vue";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import useAppStore from "@/ploutos/layouts/store/app-store";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();
  const appStore = useAppStore();

  /**
   * 菜单
   */
  const sideMenus: Ref<MenuOption[]> = ref([]);

  /**
   * 顶部+左侧模式子菜单
   */
  const { topLeftChildMenus } = storeToRefs(appStore);

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
   * 菜单
   */
  function renderMenus() {
    if (layoutStore.layoutMode == LayoutMode.TopLeft) {
      sideMenus.value = appStore.topLeftChildMenus;
    } else {
      sideMenus.value = appStore.menus;
    }
  }

  /**
   * 主题
   */
  const theme: Ref = computed(() => {
    if (layoutStore.theme == ThemeMode.DARK || layoutStore.sideTheme == SideTheme.DARK) {
      return darkTheme;
    }
    return null;
  });

  /**
   * 顶部+左侧菜单模式，侦听子菜单
   */
  watch(topLeftChildMenus, () => {
    if (layoutStore.layoutMode == LayoutMode.TopLeft) {
      sideMenus.value = appStore.topLeftChildMenus;
    }
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
