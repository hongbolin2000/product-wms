<template>
  <div class="vaw-nav-bar-wrapper" :class="themeClass">
    <NavBarHumburger/>
    <NavBarBreadCrumb v-if="layoutStore.deviceType !== 'mobile'"/>
    <div style="flex: 1"></div>

    <div class="right-wrapper" v-if="layoutStore.deviceType !== 'mobile'">
      <Actions />
    </div>

    <div class="avatar-wrapper">
      <Avatar/>
    </div>
  </div>
</template>

<script setup lang="ts">
  /********************************************************************************
   * 导航栏
   *
   * @author Berlin
   ********************************************************************************/
  import NavBarHumburger from "@/ploutos/layouts/navbar/humburger/NavBarHumburger.vue";
  import NavBarBreadCrumb from "@/ploutos/layouts/navbar/breadcrumb/NavBarBreadCrumb.vue"
  import Actions from "@/ploutos/layouts/actions/Actions.vue";
  import Avatar from "@/ploutos/layouts/avatar/Avatar.vue";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import {computed} from "vue";
  import {ThemeMode} from "@/ploutos/layouts/types.ts";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 主题
   */
  const themeClass = computed(() => {
    return layoutStore.theme === ThemeMode.LIGHT ? 'vaw-nav-bar-light-theme' : 'vaw-nav-bar-dark-theme'
  });
</script>

<style scoped lang="scss">
  .vaw-nav-bar-wrapper {
    height: $logoHeight;
    max-height: $logoHeight;
    min-height: $logoHeight;
    overflow: hidden;
    box-sizing: border-box;
    display: flex;
    align-items: center;
    margin: 0 12px;
    .avatar-wrapper {
      padding-right: 15px;
    }
    .right-wrapper {
      height: 100%;
    }
  }
  .vaw-nav-bar-light-theme {
    background-color: #fff;
  }
  .vaw-nav-bar-dark-theme {
    background-color: #18181CFF;
  }
</style>
