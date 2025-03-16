<template>
  <router-view v-slot="{ Component, route }">
    <transition name="opacity-transform" mode="out-in" appear>
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

  /**
   * 监听选项卡变化
   */
  let visitedMenus = [];
  appStore.$subscribe((mutation, state) => {
    const cacheNames = [];
    if (state.visitedMenus.length == visitedMenus.length) {
      return;
    }
    visitedMenus = [...state.visitedMenus];

    // 从路由选项卡中获取组件名
    state.visitedMenus.forEach(async (menu) => {
      let route: any = router.getRoutes().find(i => i.path == menu.key);

      // 手动匹配路由（主要是带参数的路由）
      if (route == undefined) {
        const resolved = router.resolve(menu.key);
        route = resolved.matched[resolved.matched.length - 1];
      }

      // 路由组件未加载时进行加载
      if (typeof(route?.components!.default) == 'function') {
        const components = await route?.components.default();
        cacheNames.push(components.default.name);
      } else {
        cacheNames.push(route!.components!.default.name);
      }
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
