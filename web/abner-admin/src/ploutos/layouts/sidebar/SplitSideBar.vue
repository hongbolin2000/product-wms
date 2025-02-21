<template>
  <n-config-provider :theme-overrides="themeOverThemes" :theme="theme">
    <n-card
      :bordered="false"
      class="vaw-tab-split-side-bar-wrapper"
      :content-style="{ padding: 0 }"
      style="border-radius: 0"
      :class="[
        !layoutStore.isCollapse ? 'open-status' : 'close-status',
        layoutStore.sideTheme == 'image' ? 'sidebar-bg-img' : '',
      ]"
    >
      <div class="tab-split-tab-wrapper" :style="{ backgroundColor: bgColor }">
        <Logo class="tab-split-logo-wrapper" :show-title="false"/>

        <div style="height: calc(100% - 48px)">
          <n-scrollbar>
            <div
              id="tabSplitContentWrapper"
              class="tab-split-content-wrapper"
              :style="contentWrapperStyle"
            >
              <div
                v-for="item of appStore.menus"
                :key="item.key"
                class="label-item-wrapper"
                :class="{ 'vaw-tab-split-item-is-active': item.checked }"
                @click="onTabChange(item)"
              >
                <MyIcon :name="item.icons" />
                <span>{{ item.label }}</span>
              </div>
            </div>
          </n-scrollbar>
        </div>
      </div>

      <div :class="layoutStore.isCollapse ? 'tab-split-menu-close-wrapper' : 'tab-split-menu-open-wrapper'">
        <Logo class="tab-split-logo-wrapper" :show-logo="false" v-if="!layoutStore.isCollapse"/>
        <VerticalMenu :menus="childMenus" />
      </div>
      <div class="mobile-shadow"></div>
    </n-card>
  </n-config-provider>
</template>

<script setup lang="ts">
import {computed, onMounted, ref, type Ref} from "vue";
import {useRoute, useRouter} from "vue-router";
/********************************************************************************
 * 分栏菜单
 *
 * @author Berlin
 ********************************************************************************/
import Logo from "@/ploutos/layouts/logo/Logo.vue";
import useLayoutStore from "@/ploutos/layouts/store/layout-store";
import type {MenuOption} from "@/ploutos/layouts/types";
import {SideTheme, ThemeMode} from "@/ploutos/layouts/types";
import MyIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
import VerticalMenu from "@/ploutos/layouts/menus/VerticalMenu.vue";
import useAppStore from "@/ploutos/layouts/store/app-store";
import {darkTheme} from "naive-ui";

/**
   * 全局应用状态
   */
  const appStore = useAppStore();

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 当前路由
   */
  const route = useRoute();
  const router = useRouter();

  /**
   * 选项卡子菜单
   */
  const childMenus: Ref<MenuOption[]> = ref([]);

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
   * 主题样式
   */
  const themeOverThemes = computed(() => {
    // 菜单栏背景图
    if (layoutStore.theme == ThemeMode.DARK || layoutStore.sideTheme == SideTheme.IMAGE
        || layoutStore.sideTheme == SideTheme.DARK
    ) {
      return {
        common: {
          textColor2: 'white',
        },
        Menu: {
          itemColorActiveCollapsed: layoutStore.themeColor
        }
      }
    }
    return {}
  });

  /**
   * 组件加载
   */
  onMounted(() => {
    const interval = setInterval(() => {
      if (appStore.menus.length > 0) {
        clearInterval(interval);
        matchTab();
      }
    }, 50);
  });

  /**
   * 根据路由匹配选项卡
   */
  function matchTab() {
    const menus: any[] = [];
    const matchedRoutes = route.matched;
    if (matchedRoutes && matchedRoutes.length > 0) {
      appStore.menus.forEach((menu) => {
        if (menu.key === matchedRoutes[0].path) {
          menu.checked = true
          if (menu.children) {
            menus.push(...menu.children);
          }
        } else {
          menu.checked = false
        }
      })
    }
    childMenus.value.push(...menus);
  }

  /**
   * 文本颜色
   */
  const contentWrapperStyle = computed(() => {
    return `--select-text-color: ${
      layoutStore.theme === 'light' || layoutStore.sideTheme === 'white'
        ? '#fff'
        : 'var(--text-color)'
    }`
  });

  /**
   * 选项卡背景色
   */
  const bgColor = computed(() => {

    // 菜单栏背景图
    if (layoutStore.sideTheme === SideTheme.IMAGE) {
      return 'rgba(255,255,255, 0.1)'
    }

    // 暗黑
    if (layoutStore.theme === ThemeMode.DARK || layoutStore.sideTheme === SideTheme.DARK) {
      return '#101014FF'
    }

    // 菜单栏明亮
    if (layoutStore.sideTheme === SideTheme.WHITE) {
      return '#f5f5f5'
    }
    return '#ffffff';
  });

  /**
   * 切换一级菜单
   */
  function onTabChange(item: MenuOption) {
    let menus: MenuOption[] = [];
    for (let i = 0; i < appStore.menus.length; i++) {
      const it = appStore.menus[i];
      const checked = it.key === item.key;
      it.checked = checked;

      if (checked && item.children) {
        menus = item.children;
      }
    }

    if (menus.length <= 0) {
      menus.push(item);
    }
    childMenus.value = menus;

    // 路由到第一个菜单
    for (let i = 0; i < menus.length; i++) {
      if (!menus[i].children) {
        router.push(menus[i].key);
        break;
      }
    }
  }
</script>

<style scoped lang="scss">
  .sidebar-bg-img {
    background-image: url('./menu-bg.webp') !important;
    background-size: cover;
  }
  .open-status {
    width: $tabMenuWidth;
    transition: all $transitionTime;
  }
  .close-status {
    width: $minMenuWidth;
    box-shadow: none;
    transition: all $transitionTime;
  }
  .vaw-tab-split-side-bar-wrapper {
    position: fixed;
    top: 0;
    left: 0;
    overflow: hidden;
    height: 100vh;
    box-sizing: border-box;
    z-index: 999;
    .tab-split-tab-wrapper {
      position: relative;
      top: 0;
      left: 0;
      width: $tabSplitMenuWidth;
      min-width: $tabSplitMenuWidth;
      max-width: $tabSplitMenuWidth;
      overflow: hidden;
      height: 100vh;
      box-sizing: border-box;
      animation: vaw-header-show 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
      .tab-split-logo-wrapper {
        max-width: $tabSplitMenuWidth;
        min-width: $tabSplitMenuWidth;
      }
      .tab-split-content-wrapper {
        position: relative;
        height: 100%;
        @mixin after {
          content: '';
          position: absolute;
          left: 5px;
          top: 5px;
          right: 5px;
          bottom: 5px;
          border-radius: 3px;
          z-index: -1;
        }
        .label-item-wrapper {
          position: relative;
          min-height: $logoHeight * 1.2;
          padding: 10px 0;
          display: flex;
          flex-direction: column;
          overflow: hidden;
          align-items: center;
          justify-content: center;
          box-sizing: border-box;
          color: var(--text-color);
          z-index: 1;
          & > i {
            font-size: 16px;
          }
          & > span {
            font-size: 12px;
            line-height: 14px;
            margin-top: 5px;
          }
          &:hover {
            cursor: pointer;
            color: var(--select-text-color);
          }
          &::after {
            @include after;
          }
          & svg {
            width: 26px;
            height: 26px;
          }
        }
        .label-item-wrapper:hover::after {
          background-color: var(--primary-color);
          transition: background-color $transitionTime;
        }
        .vaw-tab-split-item-is-active {
          color: var(--select-text-color);
        }
        .vaw-tab-split-item-is-active::after {
          background-color: var(--primary-color);
          @include after;
        }
      }
    }
    .tab-split-menu-open-wrapper {
      position: absolute;
      top: 0;
      right: 0;
      bottom: 0;
      left: $tabSplitMenuWidth;
      overflow: hidden;
    }
    .tab-split-menu-close-wrapper {
      position: fixed;
      left: $tabSplitMenuWidth;
      top: 0;
      max-width: $minMenuWidth;
      background-color: var(--n-color);
      height: 100%;
      padding: 10px 0;
    }
    .vaw-menu-wrapper {
      overflow-x: hidden;
      color: white;
    }
  }
  .is-mobile {
    .open-status {
      width: $tabMenuWidth;
      transform: translateX(0);
      transition: transform $transitionTime;
    }
    .close-status {
      width: calc($minMenuWidth + $tabSplitMenuWidth);
      transform: translateX(-$tabMenuWidth);
      transition: transform $transitionTime;
      box-shadow: none;
    }
  }
  @keyframes vaw-header-show {
    from {
      transform: translateX(100px);
    }
    to {
      transform: translateX(0);
    }
  }
</style>
