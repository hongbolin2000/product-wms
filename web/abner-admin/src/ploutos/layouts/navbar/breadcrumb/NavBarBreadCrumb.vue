<template>
  <n-breadcrumb>
    <n-breadcrumb-item v-for="item of breadcrumbs" :key="item.key">
      <n-dropdown
        v-if="item.children && item.children.length > 0"
        :options="item.children"
        @select="handleSelect"
      >
        <span class="dropdown-item">
          <component :is="item.icon" style="vertical-align: -0.1rem"/>
          {{ item.label }}
          <n-icon class="tip">
            <ChevronDown />
          </n-icon>
        </span>
      </n-dropdown>
      <span v-else>
        <component :is="item.icon" style="vertical-align: -0.1rem"/>
        {{ item.label }}
      </span>
    </n-breadcrumb-item>
  </n-breadcrumb>
</template>

<script setup lang="ts">
  import {useRoute, useRouter} from "vue-router";
  import {onMounted, ref, watch} from "vue";
  import { ChevronDown } from '@vicons/ionicons5';
  /********************************************************************************
   * 导航栏菜单面包屑
   *
   * @author Berlin
   ********************************************************************************/
  import useAppStore from "@/ploutos/layouts/store/app-store";
  import type {MenuOption} from "@/ploutos/layouts/types";
  import {routerHelper} from "@/ploutos";
  import {storeToRefs} from "pinia";

  /**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 当前路由
   */
  const route = useRoute();

  /**
   * 应用全局状态
   */
  const appStore = useAppStore();

  /**
   * 面包屑选项
   */
  const breadcrumbs = ref([] as MenuOption[] );

  /**
   * 菜单加载
   */
  const { menus } = storeToRefs(appStore);
  watch(menus, () => {
    generateBreadcrumb();
  });

  /**
   * 生成当前菜单的面包屑
   */
  function generateBreadcrumb() {
    breadcrumbs.value = [];
    const paths = splitPath();
    const menus = findMenu(paths);

    if (menus) {
      breadcrumbs.value = menus;
    }
  }

  /**
   * 解析每层路由路径
   */
  function splitPath(): string[] {
    const menu = appStore.expandMenus.find(i => i.key == route.path);
    const paths = menu.fullPath.split('/');
    const expireKeys: string[] = paths.filter((item) => !!item);

    return expireKeys.reduce((prev: string[], current: string) => {
      prev.push('/' + current)
      return prev;
    }, [] as string[]);
  }

  /**
   * 查找每一级菜单
   */
  function findMenu(paths: string[]) {
    const selectMenus: MenuOption[] = [];
    let menus = appStore.expandMenus;

    paths.forEach((path) => {
      const selectMenu = menus.find((item: MenuOption) => item.key === path);
      if (selectMenu) {
        menus = selectMenu.children;
        selectMenus.push(selectMenu);
      }
    })
    return selectMenus;
  }

  /**
   * 点击下拉菜单
   */
  function handleSelect(key: string) {
    router.push(key)
  }

  /**
   * 侦听菜单变化
   */
  watch(() => route.path, () => {
    if (routerHelper.isIgnoreRoute(route)) {
      return;
    }
    generateBreadcrumb();
  });
</script>

<style scoped lang="scss">
.dropdown-item {
  .tip {
    transform: rotate(0deg);
    transition: all $transitionTime;
  }
}
  .dropdown-item:hover {
    .tip {
      transform: rotate(180deg);
      transition: all $transitionTime;
    }
  }
</style>
