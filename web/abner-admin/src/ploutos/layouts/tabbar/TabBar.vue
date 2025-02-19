<template>
  <div class="vaw-tab-bar-container" ref="tabBarContainer">
    <div style="display: flex; align-items: center;padding: 6px;min-height: 49px">
      <n-icon
          class="arrow-wrapper"
          :class="{ 'arrow-wrapper__disabled': leftArrowDisabled }"
          @click="arrowClick(true)"
      >
        <ChevronBack />
      </n-icon>

      <n-scrollbar ref="scrollbar" id="tab-bar-scrollbar" :x-scrollable="true" :size="0" :on-scroll="onScroll">
        <n-button
            v-for="item of appStore.visitedMenus"
            :key="item.key"
            :type="currentPath === item.key ? 'primary' : 'default'"
            class="tab-item"
            secondary
            style="--n-color: #fff;--n-color-focus: #fff;--n-color-hover: #fff;--n-color-pressed: #fff;--n-ripple-duration: none;"
            :data="item.key"
            @click.self="itemClick(item)"
            @contextmenu="onContextMenu(item, $event)"
        >
          <span @click.self="itemClick(item)">
            <component :is="renderMenuIcon(item.icons)" style="vertical-align: -0.1rem;" @click="itemClick(item)"/>
            {{ item.label }}
          </span>
          <n-icon class="icon-item" @click="removeTab(item.key)" v-if="!item.fixed">
            <CloseOutline />
          </n-icon>
        </n-button>
      </n-scrollbar>

      <n-icon
          class="arrow-wrapper"
          :class="{ 'arrow-wrapper__disabled': rightArrowDisabled }"
          style="transform: rotate(180deg)"
          @click="arrowClick(false)"
      >
        <ChevronBack />
      </n-icon>

      <n-dropdown :options="contextMenuOptions" placement="left-start" @select="onDropDownSelect">
        <n-icon class="arrow-wrapper" @click="arrowClick">
          <Menu />
        </n-icon>
      </n-dropdown>
    </div>

    <Teleport to="body">
      <n-dropdown id="tabContextMenu" :show="showContextMenu" :options="contextMenuOptions" @select="onDropDownSelect">
        <span/>
      </n-dropdown>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
  import {ArrowBack, ArrowForward, ChevronBack, CloseOutline, Menu, Refresh, Expand, Unlink, Repeat} from '@vicons/ionicons5';
  import {h, ref, watch, onMounted, type Ref} from "vue";
  import {type RouteLocationNormalized, useRoute, useRouter} from "vue-router";
  import {NIcon, NScrollbar} from "naive-ui";
  /********************************************************************************
   * 路由选项卡组件
   *
   * @author Berlin
   ********************************************************************************/
  import useAppStore from "@/ploutos/layouts/store/app-store.ts";
  import MyIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import type {MenuOption} from "@/ploutos/layouts/types.ts";

  /**
   * 全局应用状态
   */
  const appStore = useAppStore();

  /**
   * 路由
   */
  const router = useRouter();
  const route = useRoute();

  /**
   * 当前路由路径
   */
  const currentPath = ref(route.path);

  /**
   * 容器元素
   */
  const scrollbar = ref();
  const tabBarContainer: Ref<Element | undefined> = ref();

  /**
   * 左右滚动图标是否可用
   */
  const leftArrowDisabled = ref(true);
  const rightArrowDisabled = ref(false);

  /**
   * 滚动条属性
   */
  let scrollbarProps = ref({

    /**
     * 左偏移量
     */
    scrollLeft: 0,

    /**
     * 滚动条宽度
     */
    scrollWidth: 0,

    /**
     * 滚动条容器宽度
     */
    clientWidth: 0
  });

  /**
   * 右键选项所选中的菜单
   */
  const contextCurrentMenu: Ref<MenuOption | any> = ref(undefined);

  /**
   * 右键选菜单是否显示
   */
  const showContextMenu = ref(false);

  /**
   * 右键菜单选项
   */
  const contextMenuOptions = ref([]);

  /**
   * 组件加载
   */
  onMounted(() => {
    // 进入界面滚动到当前菜单
    setTimeout(() => {
      const tabItemElements = document.getElementsByClassName("tab-item");
      if (tabItemElements.length > 0) {

        // 获取滚动条容器宽度
        const scrollbar = document.getElementById("tab-bar-scrollbar");
        const clientWidth: number = scrollbar?.clientWidth!;

        // 获取滚动条宽度
        const tabItemElement = tabItemElements[tabItemElements.length - 1] as HTMLElement;
        const scrollWidth: number = tabItemElement.parentElement?.clientWidth!;

        // 滚动条属性(检测右滚动图标是否可用)
        scrollbarProps.value = {
          scrollLeft: 0,
          scrollWidth: scrollWidth,
          clientWidth: clientWidth
        }
      }
    }, 100);

    // 将当前菜单加入选项卡
    const interval = setInterval(() => {
      if (appStore.menus.length > 0) {
        clearInterval(interval);
        addVisitedRouter({...route});
      }
    }, 50);
  });

  /**
   * 路由进入前
   */
  router.beforeEach((to: RouteLocationNormalized) => {
    addVisitedRouter(to);
  });
  function addVisitedRouter(to: RouteLocationNormalized) {
    if (["/login", "/splash", '/403', '/404'].includes(to.path)) {
      return;
    }
    if (appStore.visitedMenus.findIndex(i => i.key == to.path) != -1) {
      isDisabledArrow();
      return;
    }
    const menu = appStore.expandMenus.findLast(i => i.key == to.path);
    if (menu) {
      // 拿第一层菜单的图标
      if (!menu.icons) {
        const paths = to.path.split("/");
        const firstMenu = appStore.expandMenus.findLast(i => i.key === "/" + paths[1]);
        menu.icons = firstMenu && firstMenu.icons;
      }
    }
    appStore.visitedMenus.push(menu);
    isDisabledArrow();
  }

  /********************************************************************************
   * 滚动条Start
   ********************************************************************************/

  /**
   * 检查左右滚动图标是否显示
   */
  function isDisabledArrow() {
    const { scrollLeft, scrollWidth, clientWidth } = scrollbarProps.value;
    leftArrowDisabled.value = (scrollLeft == 0);
    rightArrowDisabled.value = Math.round(scrollLeft) == (scrollWidth - clientWidth);
  }

  /**
   * 向左/右滚动
   */
  function arrowClick(isLeft: boolean) {
    const scroll = scrollbar.value as InstanceType<typeof NScrollbar>;
    const scrollLeft = scrollbarProps.value.scrollLeft;
    scroll.scrollTo({
      left: isLeft ? Math.max(0, scrollLeft - 200) : scrollLeft + 200,
      debounce: isLeft,
      behavior: 'smooth',
    } as any, 0);
  }

  /**
   * 滚动事件
   */
  function onScroll(e: any) {
    scrollbarProps.value = {
      scrollLeft: e.target.scrollLeft,
      scrollWidth: e.target.scrollWidth,
      clientWidth: e.target.clientWidth,
    };
    isDisabledArrow();
  }

  /**
   * 侦听路由
   */
  watch(() => route.path, () => {
    currentPath.value = route.path;
    scrollToCurrentTab();
  });

  /**
   * 滚动条滚动到当前选项卡
   */
  function scrollToCurrentTab() {
    const scroll = scrollbar.value as InstanceType<typeof NScrollbar>
    const el = document.querySelector(`[data="${currentPath.value}"]`) as HTMLElement;
    el && scroll.scrollTo({
      left: el.offsetLeft,
      debounce: true,
      behavior: 'smooth',
    } as any, 0)
  }

  /********************************************************************************
   * 右键菜单Start
   ********************************************************************************/

  /**
   * 右键菜单
   */
  function onContextMenu(item: MenuOption, e: MouseEvent) {
    e.preventDefault();

    showContextMenu.value = true;
    contextCurrentMenu.value = item;
    getContextMenuOptions();

    // 更改transform属性
    setTimeout(() => {
      const tabContextMenu = document.getElementById("tabContextMenu");
      const clientHeight = tabContextMenu!.parentElement!.clientHeight;
      const style = tabContextMenu!.parentElement!.style;
      style.transform = "translateX(" + e.x + "px) translateY( " + (e.y + clientHeight) + "px) translateY(-100%)"
    }, 10);
  }

  /**
   * 右键菜单属性
   */
  function getContextMenuOptions() {
    contextMenuOptions.value = [{
      label: '内容全屏',
      key: 'fullscreen',
      icon() {
        return h(NIcon, null, {
          default: () => h(Expand),
        })
      },
    },{
      label: '刷新页面',
      key: 'refresh',
      icon() {
        return h(NIcon, null, {
          default: () => h(Refresh),
        })
      },
    },{
      label: '关闭当前',
      key: 'closeCurrent',
      disabled: contextCurrentMenu.value.fixed,
      icon() {
        return h(NIcon, null, {
          default: () => h(Unlink),
        })
      },
    },{
      label: '关闭其他',
      key: 'closeOther',
      disabled: closeLeftDisable() && closeRightDisable(),
      icon() {
        return h(NIcon, null, {
          default: () => h(Repeat),
        })
      },
    },{
      label: '关闭左侧',
      key: 'closeLeft',
      disabled: closeLeftDisable(),
      icon() {
        return h(NIcon, null, {
          default: () => h(ArrowBack),
        })
      },
    },{
      label: '关闭右侧',
      key: 'closeRight',
      disabled: closeRightDisable(),
      icon() {
        return h(NIcon, null, {
          default: () => h(ArrowForward),
        })
      },
    }, {
      label: '关闭所有',
      key: 'closeAll',
      disabled: appStore.visitedMenus.filter(i => !i.fixed).length <= 0,
      icon() {
        return h(NIcon, null, {
          default: () => h(CloseOutline),
        })
      },
    }]
  }

  /**
   * 任意点击事件关闭右键菜单
   */
  watch(showContextMenu, () => {
    if (showContextMenu) {
      document.body.addEventListener('click', closeContextMenu);
    } else {
      document.body.removeEventListener('click', closeContextMenu);
    }
  });

  /**
   * 关闭右键菜单
   */
  function closeContextMenu() {
    showContextMenu.value = false;
    contextCurrentMenu.value = undefined;
  }

  /**
   * 触发选项
   */
  function onDropDownSelect(key: string) {
    if ("refresh" == key) {
      return refreshRoute();
    }
    if ("closeCurrent" == key) {
      const path = contextCurrentMenu.value ? contextCurrentMenu.value.key : currentPath.value;
      return removeTab(path);
    }
    if ("closeOther" == key) {
      closeLeft();
      return closeRight();
    }
    if ("closeLeft" == key) {
      return closeLeft();
    }
    if ("closeRight" == key) {
      return closeRight();
    }
    if ("closeAll" == key) {
      return closeAll()
    }
  }

  /**
   * 关闭左侧
   */
  function closeLeft() {
    const currentIndex = getCurrentMenuIndex();
    appStore.visitedMenus = appStore.visitedMenus.filter((item, index) => {
      if (!item.fixed) {
        return index >= currentIndex;
      }
      return item;
    });
    toLastTabMenu();
  }

  /**
   * 关闭右侧
   */
  function closeRight() {
    const currentIndex = getCurrentMenuIndex();
    appStore.visitedMenus = appStore.visitedMenus.filter((item, index) => {
      if (!item.fixed) {
        return index <= currentIndex;
      }
      return item;
    });
    toLastTabMenu();
  }

  /**
   * 检测左侧是否还有选项卡
   */
  function closeLeftDisable() {
    const currentIndex = getCurrentMenuIndex();
    return appStore.visitedMenus.filter((item, index) => {
      return index < currentIndex && !item.fixed;
    }).length <= 0;
  }

  /**
   * 检测右侧是否还有选项卡
   */
  function closeRightDisable() {
    const currentIndex = getCurrentMenuIndex();
    return appStore.visitedMenus.filter((item, index) => {
      return index > currentIndex && !item.fixed;
    }).length <= 0;
  }

  /**
   * 获取当前菜单的索引
   */
  function getCurrentMenuIndex() {
    const key = !contextCurrentMenu.value ? currentPath.value : contextCurrentMenu.value.key;
    return appStore.visitedMenus.findIndex(i => i.key == key);
  }

  /**
   * 关闭全部
   */
  function closeAll() {
    appStore.visitedMenus = appStore.visitedMenus.filter(i => i.fixed);
    toLastTabMenu(true);
  }

  /**
   * 点击选项卡
   */
  function itemClick(item: MenuOption) {
    router.push(item.key);
  }

  /**
   * 切换到最后一个菜单
   */
  function toLastTabMenu(closeAll?: boolean) {
    if (appStore.visitedMenus.findIndex(i => i.key == currentPath.value) != -1) {
      return;
    }
    if (appStore.visitedMenus.length > 0) {
      router.push({ path: appStore.visitedMenus[appStore.visitedMenus.length -1].key });
    }
  }

  /**
   * 刷新页面
   */
  function refreshRoute() {
    router.push({ path: route.path, query: {t: new Date().getTime()} });
  }

  /**
   * 移除选项卡
   */
  function removeTab(path: string) {
    const index = appStore.visitedMenus.findIndex(i => i.key == path);
    appStore.visitedMenus.splice(index, 1);
    if (path == currentPath.value) {
      router.push(appStore.visitedMenus[appStore.visitedMenus.length - 1].key);
    }
  }

  /**
   * 菜单图标
   *
   * @param icon
   */
  function renderMenuIcon(icon: string | unknown) {
    icon = !icon ? 'menu' : icon;
    return () => h(NIcon, null, { default: () => h(MyIcon, {name: icon}) })
  }
</script>

<style scoped lang="scss">
  .vaw-tab-bar-container {
    position: relative;
    height: $tabHeight;
    line-height: calc(#{$tabHeight} - 3px);
    box-sizing: border-box;
    white-space: nowrap;
    .tab-item {
      padding: 6px 16px 4px;
      cursor: pointer;
      .icon-item {
        margin-left: 3px;
        font-size: 16px;
      }
    }
    .tab-item + .tab-item {
      margin-left: 10px;
    }
    .arrow-wrapper {
      cursor: pointer;
      font-size: 20px;
      margin: 0 8px;
    }
    .arrow-wrapper__disabled {
      cursor: not-allowed;
      color: #b9b9b9;
    }
  }
</style>