<template>
  <div class="vaw-header-layout" :class="themeClass">
    <div class="logo-wrapper">
      <Logo v-if="showLogo" :always-show="true"/>
    </div>

    <div style="flex: 1; overflow: hidden;">
      <HorizontalMenu/>
    </div>

    <div v-if="layoutStore.deviceType !== 'mobile'" class="right-wrapper">
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
  import {ThemeMode} from "@/ploutos/layouts/types.ts";

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
  const themeClass = computed(() => {
    return layoutStore.theme === ThemeMode.LIGHT ? 'vaw-header-light-theme' : 'vaw-header-dark-theme'
  });
</script>

<style scoped lang="scss">
  .vaw-header-layout {
    height: $logoHeight;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 999;
    display: flex;
    align-items: center;
    box-sizing: border-box;
    border-bottom: 1px solid var(--border-color);
    background-color: white;
    animation: left-to-right 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    .logo-wrapper {
      width: $menuWidth;
    }
    .menu-wrapper {
      flex: 1;
      overflow: hidden;
    }
    .right-wrapper {
      height: 100%;
      margin-left: 20px;
    }
    .avatar-wrapper {
      padding-right: 15px;
    }
  }
  .vaw-header-light-theme {
    background-color: #fff;
  }
  .vaw-header-dark-theme {
    background-color: #18181CFF;
  }
  @keyframes left-to-right {
    from {
      transform: translateX(100px);
    }
    to {
      transform: translateX(0);
    }
  }
</style>
