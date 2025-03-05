<template>
  <router-view v-slot="{ Component, route }">
    <transition :name="'opacity-transform'" mode="out-in" appear>
      <keep-alive :include="cacheComponentNames">
        <component :is="Component" :key="route.path" />
      </keep-alive>
    </transition>
  </router-view>
</template>

<script setup lang="ts">
  import {ref} from "vue";
  /********************************************************************************
   * 主内容
   *
   * @author Berlin
   ********************************************************************************/
  import useAppStore from "@/ploutos/layouts/store/app-store.ts";
  import {useRouter} from "vue-router";

  /**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 全局应用状态
   */
  const appStore = useAppStore();

  /**
   * 缓存的组件名
   */
  const cacheComponentNames = ref([]);

  appStore.$subscribe((mutation, state) => {
    const cacheNames = [];

    // 从路由选项卡中获取组件名
    appStore.visitedMenus.forEach(menu => {
      const route = router.getRoutes().find(i => i.path == menu.key);
      cacheNames.push(route!.components!.default['name']);
    });

    cacheComponentNames.value = cacheNames;
  });
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
