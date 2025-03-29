<template>
  <router-view v-slot="{ Component, route }">
    <transition name="opacity-transform" mode="out-in" appear>
      <keep-alive :include="cacheComponentNames">
        <component :is="renderComponent(Component, route.path)" :key="route.path" />
      </keep-alive>
    </transition>
  </router-view>
</template>

<script setup lang="ts">
  import {h, ref} from "vue";
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
   * 存放已经创建的组件
   */
  const components = new Map();

  /**
   * 包裹路由组件生成新的组件, 使用路由路径作为组件名
   *
   * @param routerComponent 需渲染的路由组件
   * @param path 路由路径
   */
  function renderComponent(routerComponent, path) {
    // 组件已经创建
    let component;
    if (components.has(path)) {
      component = components.get(path);
    }

    // 创建新的组件
    if (!component) {
      component = {
        name: path,
        render() {
          return h(routerComponent);
        },
      };
      components.set(path, component);
    }
    return h(component);
  }

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
      cacheNames.push(menu.key);
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
