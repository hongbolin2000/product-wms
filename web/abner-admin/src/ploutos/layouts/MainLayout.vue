<template>
  <div
    class="vaw-main-layout-container scrollbar"
    :class="layoutContainerClass"
  >
    <section :class="layoutTopClass">
      <NavBar/>
      <TabBar/>
    </section>

    <div class="main-base-style scrollbar" :class="[themeClass]">
      <section class="main-section" id="layout-main-section">
        <MainContent/>
      </section>

      <n-back-top :listen-to="listenTo1" />
    </div>
    <n-back-top :listen-to="listenTo2" />
  </div>
</template>

<script setup lang="ts">
  import {useRouter} from "vue-router";
  import {useLoadingBar, NIcon} from 'naive-ui';
  import {computed, onBeforeMount, h} from "vue";
  /********************************************************************************
   * 主界面布局
   *
   * @author Berlin
   ********************************************************************************/
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import NavBar from "@/ploutos/layouts/navbar/NavBar.vue";
  import {ThemeMode} from "@/ploutos/layouts/types";
  import MainContent from "@/ploutos/layouts/MainContent.vue";
  import http from "@/ploutos/layouts/axios/http";
  import layoutHelper from "@/ploutos/layouts/helps/layout-helper";
  import MyIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import type {MenuOption} from "@/ploutos/layouts/types";
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
   * 回到顶部监听
   */
  const listenTo1 = document.querySelector('.main-base-style')
  const listenTo2 = document.querySelector('.vaw-main-layout-container')

  /**
   * 布局容器样式类
   */
  const layoutContainerClass = computed(() => {
    const classList = [];
    if (layoutStore.layoutMode == 'ttb') {
      classList.push('main-layout-ttb-status');
    } else if (!layoutStore.isCollapse) {
      if (layoutStore.layoutMode == 'lcr') {
        classList.push('main-layout-lrc-open-status');
      } else {
        classList.push('main-layout-open-status');
      }
    } else {
      classList.push('main-layout-close-status');
    }

    classList.push('main-layout_fixed-nav-bar');
    return classList;
  });

  /**
   * 主内容样式类
   */
  const layoutTopClass = computed(() => {
    const classList = [];
    if (layoutStore.layoutMode == 'ttb') {
      classList.push('nav-bar-ttb-status');
    } else if (!layoutStore.isCollapse) {
      if (layoutStore.layoutMode == 'lcr') {
        classList.push('nav-bar-lcr-open-status');
      } else {
        classList.push('nav-bar-open-status');
      }
    } else {
      classList.push('nav-bar-close-status');
    }

    classList.push('fixed-nav-bar');
    classList.push(themeClass.value);
    return classList;
  });

  /**
   * 加载菜单
   */
  onBeforeMount(async () => {
    const response = await http.get("/menu/load");
    const menus: MenuOption[] = response.data;
    renderMenu(menus);
    layoutHelper.initialMenu(menus);
  });

  /**
   * 生成菜单icon
   */
  function renderMenu(menus: MenuOption[]) {
    menus.forEach((menu, index) => {
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
    return () => h(NIcon, null, { default: () => h(MyIcon, {name: icon}) })
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

  /**
   * 主题样式类
   */
  const themeClass = computed(() => {
    return layoutStore.theme === ThemeMode.LIGHT ? 'main-base-light-theme' : ''
  })
</script>

<style scoped lang="scss">
  .scrollbar::-webkit-scrollbar {
    width: 0;
  }
  .main-layout-ttb-status {
    margin-left: 0;
  }
  .main-layout-open-status {
    margin-left: $menuWidth;
  }
  .main-layout-lrc-open-status {
    margin-left: $tabMenuWidth;
  }
  .main-layout-close-status {
    margin-left: $minMenuWidth;
  }
  .nav-bar-open-status.fixed-nav-bar {
    width: calc(100% - #{$menuWidth});
  }
  .nav-bar-lcr-open-status.fixed-nav-bar {
    width: calc(100% - #{$tabMenuWidth});
  }
  .nav-bar-close-status.fixed-nav-bar {
    width: calc(100% - #{$minMenuWidth});
  }

  .nav-bar-ttb-status {
    width: 100%;
  }

  .main-layout_fixed-nav-bar {
    padding-top: $logoHeight + $tabHeight;
    overflow-y: hidden;
    .main-base-style {
      overflow-y: auto;
    }
  }

  .vaw-main-layout-container {
    height: 100%;
    box-sizing: border-box;
    transition: margin-left $transitionTime;
    .main-base-style {
      height: 100%;
      box-sizing: border-box;
      padding: 5px;
    }
    .main-base-light-theme {
      background-color: #f0f2f5;
    }
    .main-section {
      min-height: calc(100% - #{$footerHeight} - 10px);
      overflow-x: hidden;
    }
    .fixed-nav-bar {
      position: fixed;
      top: 0;
      transition: width $transitionTime;
      z-index: 99;
    }
  }
  .footer-wrapper {
    margin-top: 6px;
  }
  .is-mobile {
    .main-layout-open-status,
    .main-layout-close-status {
      margin-left: 0;
      transition: none;
    }
    .nav-bar-open-status,
    .nav-bar-close-status {
      width: 100%;
    }
  }
  .page-full-screen {
    background-color: $pageBackgroundColor;
    padding: 20px;
  }
</style>
