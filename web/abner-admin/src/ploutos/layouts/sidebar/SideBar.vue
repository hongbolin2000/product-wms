<template>
  <n-config-provider :theme="theme">
    <n-card
      class="vaw-side-bar-wrapper"
      :bordered="false"
      :style="{ borderRadius: '0px', marginTop: layoutStore.layoutMode === 'ttb' ? '48px' : 0 }"
      :content-style="{ padding: 0 }"
      :class="[
        !layoutStore.isCollapse ? 'open-status' : 'close-status',
        layoutStore.sideTheme === 'image' ? 'sidebar-bg-img' : '',
      ]"
    >
      <Logo v-if="showLogo"/>
      <ScrollerMenu/>
      <div class="mobile-shadow"></div>
    </n-card>
  </n-config-provider>
</template>

<script setup lang="ts">
  import {computed} from "vue";
  /********************************************************************************
   * 侧边栏菜单布局
   *
   * @author Berlin
   ********************************************************************************/
  import {SideTheme, ThemeMode} from '@/ploutos/layouts/types'
  import Logo from '@/ploutos/layouts/logo/Logo.vue'
  import ScrollerMenu from "@/ploutos/layouts/menus/VerticalMenu.vue";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import {darkTheme} from "naive-ui";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 父组件传入的属性
   */
  defineProps({

    /**
     * 是否显示Logo
     */
    showLogo: {
      type: Boolean,
      default: true
    }
  });

  /**
   * 主题
   */
  const theme = computed(() => {
    if (layoutStore.theme == ThemeMode.DARK || layoutStore.sideTheme == SideTheme.DARK) {
      return darkTheme;
    }
    return null;
  });
</script>

<style scoped lang="scss">
  .vaw-side-bar-wrapper {
    position: fixed;
    top: 0;
    left: 0;
    overflow-x: hidden;
    height: 100%;
    box-sizing: border-box;
    z-index: 999;
    .vaw-menu-wrapper {
      overflow-x: hidden;
      color: white;
    }
  }
  .open-status {
    width: $menuWidth;
    transition: all $transitionTime;
  }
  .close-status {
    width: $minMenuWidth;
    box-shadow: none;
    transition: all $transitionTime;
  }
  .is-mobile {
    .open-status {
      width: $menuWidth;
      transform: translateX(0);
      transition: transform $transitionTime;
    }
    .close-status {
      width: $menuWidth;
      $negativeMenuWidth: calc(#{$menuWidth} * -1);
      transform: translateX($negativeMenuWidth);
      transition: transform $transitionTime;
      box-shadow: none;
    }
  }
  .sidebar-bg-img {
    background-image: url('./menu-bg.webp') !important;
    background-size: cover;
  }
</style>
