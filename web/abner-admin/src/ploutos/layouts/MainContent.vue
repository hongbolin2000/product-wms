<template>
  <router-view v-slot="{ Component, route }">
    <transition :name="'opacity-transform'" mode="out-in" appear>
      <keep-alive>
        <component :is="Component" :key="route.path" />
      </keep-alive>
    </transition>
  </router-view>
</template>

<script setup lang="ts">
  /********************************************************************************
   * 主内容
   *
   * @author Berlin
   ********************************************************************************/
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();
</script>

<style lang="scss">
  /* 渐隐渐现页面动画效果 */
  .opacity-transform-leave-active,
  .opacity-transform-enter-active {
    transition: all 0.5s;
  }
  .opacity-transform-enter-from {
    opacity: 0;
  }
  .opacity-transform-leave-to {
    opacity: 0;
  }
</style>
