<template>
  <div class="vaw-nav-bar-wrapper" :style="{backgroundColor: bgColor}">
    <NavBarHumburger/>
    <NavBarBreadCrumb v-if="layoutStore.deviceType !== DeviceType.MOBILE"/>
    <div style="flex: 1"></div>

    <div v-if="layoutStore.deviceType !== DeviceType.MOBILE">
      <Actions />
    </div>

    <div class="avatar-wrapper">
      <Avatar/>
    </div>
  </div>
</template>

<script setup lang="ts">
  import {computed} from "vue";
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
  import {DeviceType, ThemeMode} from "@/ploutos/layouts/types";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 导航栏背景色
   */
  const bgColor = computed(() => {
    if (layoutStore.theme === ThemeMode.LIGHT) {
      return '#fff';
    } else {
      return '#18181CFF';
    }
  });
</script>

<style scoped lang="scss">
  .vaw-nav-bar-wrapper {
    min-height: $logoHeight;
    display: flex;
    align-items: center;
    margin: 0 10px;
    .avatar-wrapper {
      padding-right: 10px;
    }
  }
</style>
