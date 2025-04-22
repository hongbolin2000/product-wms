<template>
  <n-config-provider :theme-overrides="themeOverThemes" :theme="theme">
    <n-card
      class="vaw-tab-split-side-bar-wrapper"
      :content-style="{ padding: 0 }"
      :bordered="layoutStore.sideTheme != SideTheme.IMAGE"
      style="border-radius: 0;"
      :class="[
        layoutStore.isCollapse ? 'close-status' : 'open-status',
        layoutStore.sideTheme == SideTheme.IMAGE ? 'sidebar-bg-img' : '',
      ]"
    >
      <div class="tab-split-tab-wrapper" :style="{ backgroundColor: bgColor }">
        <Logo :show-title="false"/>

        <div class="tab-split-tab-scroll">
          <n-scrollbar>
            <div class="tab-split-content-wrapper" :style="contentWrapperStyle">
              <div
                v-for="item of appStore.menus"
                :key="item.key"
                class="label-item-wrapper"
                :class="{ 'vaw-tab-split-item-is-active': item.checked }"
                @click="onTabChange(item)"
              >
                <SvgIcon :name="item.icons" />
                <span>{{ item.label }}</span>
              </div>
            </div>
          </n-scrollbar>
        </div>
      </div>

      <div :class="layoutStore.isCollapse ? 'tab-split-menu-close-wrapper' : 'tab-split-menu-open-wrapper'">
        <Logo :show-logo="false" v-if="!layoutStore.isCollapse"/>
        <VerticalMenu
            :vertical-menus="childMenus"
            :class="[layoutStore.sideTheme == SideTheme.IMAGE ? 'sidebar-bg-img' : '']"
        />
      </div>
      <div class="mobile-shadow"></div>
    </n-card>
  </n-config-provider>
</template>

<script setup lang="ts">
  import {computed, ref, type Ref, watch, onMounted} from "vue";
  import {useRoute, useRouter} from "vue-router";
  import {darkTheme} from "naive-ui";
  /********************************************************************************
   * 分栏菜单
   *
   * @author Berlin
   ********************************************************************************/
  import Logo from "@/ploutos/layouts/logo/Logo.vue";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import {type MenuOption, SideTheme, ThemeMode} from "@/ploutos/layouts/types";
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import VerticalMenu from "@/ploutos/layouts/menus/VerticalMenu.vue";
  import useAppStore from "@/ploutos/layouts/store/app-store";
  import {storeToRefs} from "pinia";

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
  const theme: Ref = computed(() => {
    if (layoutStore.theme == ThemeMode.DARK || layoutStore.sideTheme == SideTheme.DARK) {
      return darkTheme;
    }
    return null;
  });

  /**
   * 主题样式
   */
  const themeOverThemes = computed(() => {
    // 暗黑模式和菜单栏背景图
    if (layoutStore.theme == ThemeMode.DARK || layoutStore.sideTheme == SideTheme.IMAGE
        || layoutStore.sideTheme == SideTheme.DARK
    ) {
      return {
        common: {
          textColor2: 'white', // 文字颜色
        },
        Menu: {
          // 菜单折叠激活时背景颜色
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
    if (appStore.menus.length > 0) {
      matchTab();
    }
  });

  /**
   * 菜单加载
   */
  const { menus } = storeToRefs(appStore);
  watch(menus, () => {
    matchTab();
  });

  /**
   * 根据路由匹配选项卡
   */
  function matchTab() {
    // 获取匹配到的路由
    const menus: MenuOption[] = [];
    const currentMenu: MenuOption = appStore.expandMenus.find(i => i.key == route.fullPath);

    // 未找到菜单
    if (!currentMenu) {
      return;
    }

    // 找到子路由
    appStore.menus.forEach((menu) => {
      if (!menu.children && menu.key == currentMenu.fullUrl) {
        menu.checked = true;
        menus.push(menu);
        return;
      }
      if (menu.key == '/' + currentMenu.fullUrl.split("/")[1]) {
        menu.checked = true
        if (menu.children) {
          menus.push(...menu.children);
        } else {
          menus.push(menu);
        }
      } else {
        menu.checked = false
      }
    })
    childMenus.value = menus;
  }

  /**
   * 文本颜色
   */
  const contentWrapperStyle = computed(() => {
    let color = 'var(--text-color)';
    if (layoutStore.theme === ThemeMode.LIGHT || layoutStore.sideTheme == SideTheme.WHITE) {
      color = 'white'
    }
    return `--select-text-color: ${color }`
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
  });

  /**
   * 切换一级菜单
   */
  function onTabChange(item: MenuOption) {
    let menus: MenuOption[] = [];

    // 找到子菜单
    for (let i = 0; i < appStore.menus.length; i++) {
      const it = appStore.menus[i];
      const checked = it.key === item.key;
      it.checked = checked;

      if (checked && item.children) {
        menus = item.children;
      }
    }

    // 没有子菜单
    if (menus.length <= 0) {
      menus.push(item);
    }
    childMenus.value = menus;
  }

  /**
   * 路由改变
   */
  watch(() => route.fullPath, () => {
    matchTab();
  });
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
    transition: all $transitionTime;
  }

  .vaw-tab-split-side-bar-wrapper {
    position: fixed;
    z-index: 999;
    .tab-split-tab-wrapper {
      max-width: $tabSplitMenuWidth;
      height: 100vh;
      animation: vaw-header-show 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
      .tab-split-tab-scroll {
        height: calc(100% - $logoHeight);
      }
      .tab-split-content-wrapper {
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
          min-height: calc($logoHeight * 1.2);
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          z-index: 1;
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
          &:hover::after {
            background-color: var(--primary-color);
            transition: background-color $transitionTime;
          }
        }
        .vaw-tab-split-item-is-active {
          color: var(--select-text-color);
          &::after {
            background-color: var(--primary-color);
          }
        }
      }
    }
    .tab-split-menu-open-wrapper {
      position: absolute;
      right: 0;
      bottom: 0;
      left: $tabSplitMenuWidth;
    }
    .tab-split-menu-close-wrapper {
      position: fixed;
      left: $tabSplitMenuWidth;
      top: 0;
      max-width: $minMenuWidth;
      background-color: var(--n-color);
      height: 100%;
      transition: all $transitionTime;
    }
  }

  .is-mobile {
    .close-status {
      $negativeMenuWidth: calc(#{$tabSplitMenuWidth + $minMenuWidth} * -1);
      transform: translateX($negativeMenuWidth);
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
