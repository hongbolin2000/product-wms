<template>
  <n-config-provider :theme-overrides="themeOverThemes">
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
      <transition name="logo">
        <Logo v-if="showLogo"/>
      </transition>
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
  import {ThemeMode, SideTheme} from '@/layouts/types'
  import Logo from '@/layouts/logo/Logo.vue'
  import ScrollerMenu from "@/layouts/menus/VerticalMenu.vue";
  import {useLayoutStore} from "@/layouts/store/layout-store";

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
   * 重写主题样式
   */
  const themeOverThemes = computed(() => {

    // 布局暗黑主题
    if (layoutStore.theme === ThemeMode.DARK) {
      return {}
    }

    // 菜单侧栏暗黑主题
    if (layoutStore.sideTheme === SideTheme.DARK) {
      return {
        common: {
          cardColor: '#001428',
          textColor1: '#bbbbbb',
          textColor2: '#bbbbbb',
          popoverColor: 'rgb(72, 72, 78)',
          hoverColor: 'rgba(255, 255, 255, 0.09)',
          itemColorActive: 'rgba(24, 160, 88, 0.4)',
        },
        Menu: {
          itemTextColorChildActive: '#ffffff',
          itemIconColorChildActive: '#ffffff',

          arrowColorChildActive: '#ffffff',
          arrowColorHover: '#ffffff',

          itemTextColorActive: '#ffffff',
          itemIconColorActive: '#ffffff',

          itemTextColorHover: '#ffffff',
          itemIconColorHover: '#ffffff',

          itemColorActive: 'var(--primary-color)',
        },
      }
    }
    // 菜单侧栏纯白主题
    if (layoutStore.sideTheme === SideTheme.WHITE) {
      return {
        common: {
          cardColor: '#ffffff'
        }
      }
    }
    // 菜单侧栏背景图主题
    if (layoutStore.sideTheme === SideTheme.IMAGE) {
      return {
        common: {
          textColor1: '#bbbbbb',
          textColor2: '#bbbbbb',
          hoverColor: 'rgba(255, 255, 255, 0.09)',
          popoverColor: 'rgb(72, 72, 78)',
        },
      }
    }
    return {}
  })
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
    box-shadow: 2px 5px 10px rgba(0, 0, 0, 0.12);
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
