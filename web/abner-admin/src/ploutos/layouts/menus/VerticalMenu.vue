<template>
  <n-config-provider :theme-overrides="themeOverThemes">
    <div :class="menuClass">
      <n-scrollbar>
        <n-menu
          mode="vertical"
          :value="currentPath"
          :collapsed="layoutStore.isCollapse"
          :options="verticalMenus"
          :expanded-keys="expandKeys"
          :collapsed-icon-size="16"
          :collapsed-width="63"
          :indent="21"
          accordion
          @update:value="onMenuClick"
          @update:expanded-keys="onMenuExpanded"
        />
      </n-scrollbar>
    </div>
  </n-config-provider>
</template>

<script setup lang="ts">
  import {computed, ref, watch, onMounted} from "vue";
  import {useRoute, useRouter} from "vue-router";
  import {darkTheme} from "naive-ui";
  import {storeToRefs} from "pinia";
  /********************************************************************************
   * 垂直菜单
   *
   * @author Berlin
   ********************************************************************************/
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import {DeviceType, LayoutMode, type MenuOption, SideTheme, ThemeMode} from "@/ploutos/layouts/types";
  import useAppStore from "@/ploutos/layouts/store/app-store.ts";

  /**
   * 父组件传入的属性
   */
  defineProps({

    /**
     * 菜单
     */
    verticalMenus: {
      type: Array<MenuOption>,
      required: true
    }
  });

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();
  const appStore = useAppStore();

  /**
   * 当前路由
   */
  const currentRoute = useRoute();

  /**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 当前路由地址
   */
  const currentPath = ref(currentRoute.fullPath);

  /**
   * 展开的菜单
   */
  const expandKeys = ref(['']);

  /**
   * 主题
   */
  const theme = computed(() => {
    if (layoutStore.theme == ThemeMode.DARK || layoutStore.sideTheme == SideTheme.DARK) {
      return darkTheme;
    }
    return null;
  });

  /**
   * 重写主题样式
   */
  const themeOverThemes = computed(() => {

    // 暗黑模式
    if (layoutStore.theme == ThemeMode.DARK || layoutStore.sideTheme == SideTheme.DARK ||
        layoutStore.sideTheme == SideTheme.IMAGE
    ) {
      return {
        common: {
          textColor1: '#bbbbbb', // 图标
          textColor2: '#bbbbbb', // 文字
          hoverColor: 'none', // 鼠标经过背景
        },
        Menu: {
          // 父菜单
          itemTextColorChildActive: '#fff', // 激活时文字颜色
          itemIconColorChildActive: '#fff', // 激活时图标颜色
          arrowColorChildActive: '#fff', // 激活时箭头颜色
          itemTextColorChildActiveHover: '#fff', // 激活时鼠标经过文字颜色
          itemIconColorChildActiveHover: '#fff', // 激活时鼠标经过图标颜色
          arrowColorChildActiveHover: '#fff', // 激活时鼠标经过箭头颜色

          // 子菜单
          itemTextColorActive: '#fff', // 激活时文字高亮颜色
          itemIconColorActive: '#fff', // 激活时图标高亮颜色
          itemTextColorHover: '#fff', // 鼠标经过文字高亮颜色
          itemIconColorHover: '#fff', // 鼠标经过图标高亮颜色
          itemColorActiveHover: layoutStore.themeColor, // 激活时鼠标经过背景颜色
          itemTextColorActiveHover: '#fff', // 激活时鼠标经过文字颜色
          itemIconColorActiveHover: '#fff', // 激活时鼠标经过图标颜色

          itemColorActive: layoutStore.themeColor, // 激活时背景颜色
        },
        Dropdown: {
          // 子菜单
          optionTextColorHover: '#fff', // 鼠标经过文字颜色
          optionTextColorActive: '#fff', // 激活时文字颜色
          optionTextColorChildActive: '#fff', // 激活时子菜单的父菜单文字颜色
          optionColorActive: layoutStore.themeColor // 激活时背景颜色
        }
      }
    }
    return {}
  });

  /**
   * 菜单样式类
   */
  const menuClass = computed(() => {
    if (layoutStore.layoutMode == LayoutMode.LeftSplit && layoutStore.isCollapse) {
      return 'left-split-collapse-menu';
    }
    return 'vaw-vertical-menu';
  });

  /**
   * 组件加载
   */
  onMounted(() => {
    if (appStore.menus.length > 0) {
      handleExpandPath();
    }
  });

  /**
   * 菜单加载
   */
  const { menus } = storeToRefs(appStore);
  watch(menus, () => {
    handleExpandPath();
  });

  /**
   * 计算展开的菜单
   */
  function handleExpandPath() {
    const menu = appStore.expandMenus.find(i => i.key == currentPath.value);
    if (!menu) {
      return;
    }

    // 分隔出每一级路由菜单
    const paths = menu.fullUrl.split('/');
    let expands: string[] = paths.filter((item) => !!item);

    // 将每一级路由菜单进行拼接
    expands = expands.reduce((prev: string[], current: string) => {
      const lastItem = prev[prev.length - 1]
      if (!lastItem) {
        prev.push('/' + current)
      } else {
        prev.push(lastItem + '/' + current)
      }
      return prev
    }, [] as string[]);
    expandKeys.value = expands;
  }

  /**
   * 展开菜单
   */
  function onMenuExpanded(keys: string[]) {
    expandKeys.value = keys;
  }

  /**
   * 点击菜单
   */
  function onMenuClick(key: string) {
    router.push(key);
    if (layoutStore.deviceType === DeviceType.MOBILE) {
      layoutStore.isCollapse = true;
    }
  }

  /**
   * 监听路由变化
   */
  watch(() => currentRoute.fullPath, (value) => {
    if (appStore.expandMenus.find(i => i.key == value)?.label) {
      currentPath.value = value;
      handleExpandPath();
    }
  });
</script>

<style scoped lang="scss">
  .vaw-vertical-menu {
    height: calc(100vh - #{$logoHeight}) !important;
  }
   .left-split-collapse-menu {
     height: 100vh;
   }
</style>
