<template>
  <router-view v-slot="{ Component, route }">
    <transition :name="layoutStore.pageAnimate + '-transform'" mode="out-in" appear>
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
