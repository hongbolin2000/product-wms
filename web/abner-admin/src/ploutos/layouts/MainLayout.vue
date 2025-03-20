<template>
  <div class="vaw-main-layout-container" :class="layoutContainerClass">
    <section :class="layoutTopClass" :style="{backgroundColor: layoutStore.themeBgColor, top: paddingTop}">
      <NavBar v-if="showNavBar"/>
      <TabBar/>
    </section>

    <section class="main-section">
      <MainContent id="layout-main-content"/>
    </section>
  </div>
</template>

<script setup lang="ts">
  import {useRouter} from "vue-router";
  import {NIcon, useLoadingBar} from 'naive-ui';
  import {computed, h, onBeforeMount} from "vue";
  /********************************************************************************
   * 主界面布局
   *
   * @author Berlin
   ********************************************************************************/
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import NavBar from "@/ploutos/layouts/navbar/NavBar.vue";
  import {LayoutMode, type MenuOption} from "@/ploutos/layouts/types";
  import MainContent from "@/ploutos/layouts/MainContent.vue";
  import http from "@/ploutos/layouts/axios/http";
  import layoutHelper from "@/ploutos/layouts/helps/layout-helper";
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import TabBar from "@/ploutos/layouts/tabbar/TabBar.vue";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 路由loading对象
   */
  const loadingBar = useLoadingBar();

  /**
   * 布局容器样式类
   */
  const layoutContainerClass = computed(() => {
    const classList = [];

    // 上下布局
    if (layoutStore.layoutMode == LayoutMode.TopBottom) {
      classList.push('main-layout-ttb-status');
    } else if (!layoutStore.isCollapse) {

      // 分栏布局打开菜单
      if (layoutStore.layoutMode == LayoutMode.LeftSplit) {
        classList.push('main-layout-lcr-open-status');
      } else {
        classList.push('main-layout-open-status');
      }
    } else {

      // 分栏布局关闭菜单
      if (layoutStore.layoutMode == LayoutMode.LeftSplit) {
        classList.push('main-layout-lcr-close-status');
      } else {
        classList.push('main-layout-close-status');
      }
    }

    classList.push('main-layout_fixed-nav-bar');
    return classList;
  });

  /**
   * 顶部样式类
   */
  const layoutTopClass = computed(() => {
    const classList = [];

    // 上下布局
    if (layoutStore.layoutMode == LayoutMode.TopBottom) {
      classList.push('nav-bar-ttb-status');
    } else if (!layoutStore.isCollapse) {

      // 分栏布局
      if (layoutStore.layoutMode == LayoutMode.LeftSplit) {
        classList.push('nav-bar-lcr-open-status');
      } else {
        classList.push('nav-bar-open-status');
      }
    } else {
      if (layoutStore.layoutMode == LayoutMode.LeftSplit) {
        classList.push('nav-bar-lcr-close-status');
      } else {
        classList.push('nav-bar-close-status');
      }
    }

    classList.push('fixed-nav-bar');
    return classList;
  });

  /**
   * 导航栏距离顶部距离
   */
  const paddingTop = computed(() => {
    // 上下布局padding
    if (layoutStore.layoutMode == LayoutMode.TopBottom ||
        layoutStore.layoutMode == LayoutMode.TopLeft
    ) {
      return '64px';
    }
    return '0px';
  });

  /**
   * 是否显示导航栏
   */
  const showNavBar = computed(() => {
    // 上下布局不显示
    return layoutStore.layoutMode !== LayoutMode.TopBottom &&
        layoutStore.layoutMode != LayoutMode.TopLeft
  });

  /**
   * 加载菜单
   */
  onBeforeMount(async () => {
    const response = await http.get("/menu/load/pc");
    const menus: MenuOption[] = response.data;
    renderMenu(menus);
    layoutHelper.initialMenu(menus);
  });

  /**
   * 生成菜单icon
   */
  function renderMenu(menus: MenuOption[]) {
    menus.forEach((menu) => {
      menu.icon = renderIcon(menu.icons);
      if (menu.children && menu.children.length > 0) {
        renderMenu(menu.children);
      } else {
        delete menu.children;
      }
    });
  }

  /**
   * 菜单图标
   *
   * @param icon
   */
  function renderIcon(icon: string) {
    icon = !icon ? 'menu' : icon;
    return () => h(NIcon, null, { default: () => h(SvgIcon, {name: icon}) })
  }

  /**
   * 路由进入前
   */
  router.beforeEach(() => {
    loadingBar.start();
  });

  /**
   * 路由加载完成
   */
  router.afterEach(() => {
    loadingBar.finish();
  });
</script>

<style scoped lang="scss">
  // 上下布局
  .main-layout-ttb-status {
    margin-left: 0;
  }
  // 左右布局打开菜单
  .main-layout-open-status {
    margin-left: $menuWidth;
  }
  // 分栏布局打开菜单
  .main-layout-lcr-open-status {
    margin-left: $tabMenuWidth;
  }
  // 左右布局关闭菜单
  .main-layout-close-status {
    margin-left: $minMenuWidth;
  }
  // 分栏布局关闭菜单
  .main-layout-lcr-close-status {
    margin-left: calc($minMenuWidth + $tabSplitMenuWidth);
  }
  // 固定顶部导航栏
  .main-layout_fixed-nav-bar {
    padding-top: calc($logoHeight + $tabHeight);
  }

  // 上下布局
  .nav-bar-ttb-status {
    width: 100%;
  }
  // 左右布局打开菜单
  .nav-bar-open-status.fixed-nav-bar {
    width: calc(100% - #{$menuWidth});
  }
  // 分栏布局打开菜单
  .nav-bar-lcr-open-status.fixed-nav-bar {
    width: calc(100% - #{$tabMenuWidth});
  }
  // 左右布局关闭菜单
  .nav-bar-close-status.fixed-nav-bar {
    width: calc(100% - #{$minMenuWidth});
  }
  // 分栏布局关闭菜单
  .nav-bar-lcr-close-status.fixed-nav-bar {
    width: calc(100% - (#{$minMenuWidth} + #{$tabSplitMenuWidth}));
  }
  // 固定导航栏
  .vaw-main-layout-container {
    .fixed-nav-bar {
      position: fixed;
      transition: width $transitionTime;
      z-index: 1;
    }
  }

  .vaw-main-layout-container {
    transition: margin-left $transitionTime;
    .main-section {
      padding: 0 10px 10px 10px;
    }
  }

  // 手机模式
  .is-mobile {
    // 打开/关闭菜单
    .main-layout-open-status,
    .main-layout-close-status,
    .main-layout-lcr-open-status,
    .main-layout-lcr-close-status {
      margin-left: 0;
      transition: none;
    }

    // 打开/关闭菜单
    .nav-bar-open-status,
    .nav-bar-close-status,
    .nav-bar-lcr-open-status,
    .nav-bar-lcr-close-status{
      width: 100%;
    }
  }
  .page-full-screen-light {
    padding: 10px;
    background-color: #f0f2f5;
  }
  .page-full-screen-dark {
    padding: 10px;
    background-color: #101014FF;
  }
</style>
