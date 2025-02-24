<template>
  <div class="vaw-header-layout" :class="themeClass">
    <div :class="layoutStore.isCollapse ? 'logo-wrapper-close' : 'logo-wrapper-open'">
      <Logo v-if="layoutStore.layoutMode == LayoutMode.TopBottom"/>
    </div>

    <NavBarHumburger style="margin: 0"/>

    <div style="flex: 1; overflow: hidden;">
      <HorizontalMenu/>
    </div>

    <div v-if="layoutStore.deviceType !== DeviceType.MOBILE" style="margin-left: 10px">
      <Actions />
    </div>

    <div class="avatar-wrapper">
      <Avatar />
    </div>
  </div>
</template>

<script setup lang="ts">
  /********************************************************************************
   * 横向模式菜单布局
   *
   * @author Berlin
   ********************************************************************************/
  import Logo from '@/ploutos/layouts/logo/Logo.vue';
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import Actions from "@/ploutos/layouts/actions/Actions.vue";
  import Avatar from "@/ploutos/layouts/avatar/Avatar.vue";
  import HorizontalMenu from "@/ploutos/layouts/menus/HorizontalMenu.vue";
  import {computed} from "vue";
  import {DeviceType, LayoutMode, ThemeMode} from "@/ploutos/layouts/types.ts";
  import NavBarHumburger from "@/ploutos/layouts/navbar/humburger/NavBarHumburger.vue";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 主题
   */
  const themeClass = computed(() => {
    return layoutStore.theme === ThemeMode.LIGHT ? 'vaw-header-light-theme' : 'vaw-header-dark-theme'
  });
</script>

<style scoped lang="scss">
  .vaw-header-layout {
    height: $logoHeight;
    position: fixed;
    left: 0;
    right: 0;
    display: flex;
    align-items: center;
    animation: vaw-header-show 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    .logo-wrapper-open {
      width: $menuWidth;
    }
    .logo-wrapper-close {
      width: $minMenuWidth;
    }
    .menu-wrapper {
      flex: 1;
      overflow: hidden;
    }
    .avatar-wrapper {
      padding-right: 15px;
    }
  }
  .is-mobile {
    .logo-wrapper {
      width: 65px;
    }
  }
  .vaw-header-light-theme {
    background-color: #fff;
  }
  .vaw-header-dark-theme {
    background-color: #18181CFF;
  }
  @keyframes vaw-header-show {
    from {
      transform: translateX(100px);
    }
    to {
      transform: translateX(0);
    }
  }
</style>
