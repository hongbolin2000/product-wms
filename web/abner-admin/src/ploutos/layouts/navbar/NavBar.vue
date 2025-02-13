<template>
  <div class="vaw-nav-bar-wrapper">
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

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();
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
    border-bottom: 1px solid var(--border-color);
    .avatar-wrapper {
      padding-right: 15px;
    }
    .right-wrapper {
      height: 100%;
    }
  }
</style>
