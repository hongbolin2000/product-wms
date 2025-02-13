<template>
  <div class="vaw-header-layout">
    <div class="logo-wrapper">
      <Logo v-if="showLogo"/>
    </div>

    <div style="flex: 1; overflow: hidden; padding: 0 10px">
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
    .logo-wrapper {
      width: calc(#{$menuWidth} / 3 * 2);
    }
    .menu-wrapper {
      flex: 1;
      overflow: hidden;
    }
    .right-wrapper {
      height: 100%;
    }
    .avatar-wrapper {
      padding-right: 15px;
    }
  }
</style>
