<template>
  <div
    class="vaw-main-layout-container scrollbar"
    :class="[
      layoutStore.layoutMode === 'ttb'
        ? 'main-layout-ttb-status'
        : !layoutStore.isCollapse
        ? 'main-layout-open-status'
        : 'main-layout-close-status',
      layoutStore.isFixedNavBar ? 'main-layout_fixed-nav-bar' : 'main-layout',
    ]"
  >
    <section
      :class="[
        layoutStore.layoutMode === 'ttb'
          ? 'nav-bar-ttb-status'
          : !layoutStore.isCollapse
          ? 'nav-bar-open-status'
          : 'nav-bar-close-status',
        layoutStore.isFixedNavBar ? 'fixed-nav-bar' : '',
        !showNavBar ? 'tab-bar-top' : '',
      ]"
    >
      <NavBar v-if="showNavBar" />
    </section>

    <div class="main-base-style scrollbar" :class="[mainClass]">
      <section class="main-section">
        <MainContent/>
      </section>

      <n-back-top :listen-to="listenTo1" />
    </div>
    <n-back-top :listen-to="listenTo2" />
  </div>
</template>

<script setup lang="ts">
  import {useRouter} from "vue-router";
  import {useLoadingBar} from 'naive-ui';
  import {computed} from "vue";
  /********************************************************************************
   * 主界面布局
   *
   * @author Berlin
   ********************************************************************************/
  import {useLayoutStore} from "@/layouts/store/layout-store"
  import NavBar from "@/layouts/navbar/NavBar.vue";
  import {ThemeMode} from "@/layouts/types";
  import MainContent from "@/layouts/MainContent.vue";

  /**
   * 父组件传入的属性
   */
  defineProps({

    /**
     * 是否显示导航栏
     */
    showNavBar: {
      type: Boolean,
      default: true
    }
  });

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
  const mainClass = computed(() => {
    return layoutStore.theme === ThemeMode.DARK ? 'main-base-dark-theme' : 'main-base-light-theme'
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
  .main-layout-close-status {
    margin-left: $minMenuWidth;
  }
  .nav-bar-open-status.fixed-nav-bar {
    width: calc(100% - #{$menuWidth});
  }
  .nav-bar-close-status.fixed-nav-bar {
    width: calc(100% - #{$minMenuWidth});
  }

  .nav-bar-ttb-status {
    width: 100%;
  }

  .main-layout {
    padding-top: 0;
    overflow-y: auto;
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
    .main-base-dark-theme {
      background-color: #333333;
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
    .tab-bar-top {
      padding-top: $logoHeight;
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
</style>
