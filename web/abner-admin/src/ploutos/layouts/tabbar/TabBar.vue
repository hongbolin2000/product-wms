<template>
  <div class="vaw-tab-bar-container">
    <n-icon class="arrow-wrapper" @click="arrowClick(true)"
        :class="{ 'arrow-wrapper__disabled': leftArrowDisabled }"
    >
      <ChevronBack />
    </n-icon>

    <n-scrollbar ref="scrollbar" :on-scroll="onScroll">
      <n-button
          v-for="item of appStore.visitedMenus"
          :key="item.key"
          :type="currentPath == item.key ? 'primary' : 'default'"
          class="tab-item"
          secondary
          :style="buttonStyle(item)"
          :data="item.key"
          @click.self="itemClick(item)"
          @contextmenu="onContextMenu(item, $event)"
      >
        <span @click.self="itemClick(item)">
          <component :is="renderMenuIcon(item.icons)" @click="itemClick(item)"/>
          {{ item.label }}
        </span>
        <n-icon class="icon-item" @click="removeTab(item.key)" v-if="!item.fixed && appStore.visitedMenus.length != 1">
          <CloseOutline />
        </n-icon>
      </n-button>
    </n-scrollbar>

    <n-icon class="arrow-wrapper" style="transform: rotate(180deg)" @click="arrowClick(false)"
        :class="{ 'arrow-wrapper__disabled': rightArrowDisabled }"
    >
      <ChevronBack />
    </n-icon>

    <n-dropdown :options="contextMenuOptions" placement="left-start" @select="onDropDownSelect">
      <n-icon class="arrow-wrapper">
        <Menu />
      </n-icon>
    </n-dropdown>

    <n-dropdown
        :x="x" :y="y"
        trigger="manual"
        placement="right-start"
        :show="showContextMenu"
        :options="contextMenuOptions"
        @select="onDropDownSelect"
        :on-clickoutside="contextMenuClickOutSide"
    />
  </div>
</template>

<script setup lang="ts">
  import {
    ArrowBack,
    ArrowForward,
    ChevronBack,
    CloseOutline,
    Expand,
    Menu,
    Refresh,
    Repeat,
    Unlink
  } from '@vicons/ionicons5';
  import {h, type Ref, ref, watch} from "vue";
  import {type RouteLocationNormalized, useRoute, useRouter} from "vue-router";
  import {type DropdownOption, NIcon, NScrollbar} from "naive-ui";
  /********************************************************************************
   * 路由选项卡组件
   *
   * @author Berlin
   ********************************************************************************/
  import useAppStore from "@/ploutos/layouts/store/app-store";
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import {type MenuOption, ThemeMode} from "@/ploutos/layouts/types";
  import {screen, routerHelper} from "@/ploutos";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import {storeToRefs} from "pinia";

  /**
   * 全局应用状态
   */
  const appStore = useAppStore();
  const layoutStore = useLayoutStore();

  /**
   * 路由
   */
  const router = useRouter();
  const route = useRoute();

  /**
   * 当前路由路径
   */
  const currentPath = ref(route.fullPath);

  /**
   * 滚动条容器元素
   */
  const scrollbar = ref();

  /**
   * 左右滚动图标是否可用
   */
  const leftArrowDisabled = ref(true);
  const rightArrowDisabled = ref(true);

  /**
   * 滚动条属性
   */
  let scrollbarProps = ref({

    /**
     * 左偏移量
     */
    scrollLeft: 0,

    /**
     * 滚动条总长度
     */
    scrollWidth: 0,

    /**
     * 滚动条容器宽度
     */
    clientWidth: 0
  });

  /**
   * 右键菜单是否显示
   */
  const showContextMenu = ref(false);

  /**
   * 右键菜单选项
   */
  const contextMenuOptions: Ref<DropdownOption[]> = ref([]);

  /**
   * 右键选项所选中的菜单
   */
  const contextCurrentMenu: Ref<MenuOption | any> = ref(undefined);

  /**
   * 右键菜单位置
   */
  const x = ref(0);
  const y = ref(0);

  /**
   * 按钮样式
   */
  function buttonStyle(item: MenuOption) {
    let style;
    if (layoutStore.theme == ThemeMode.LIGHT) {
      style = '--n-color: #fff;--n-color-focus: #fff;--n-color-hover: #fff;--n-color-pressed: #fff;'
      style += '--n-ripple-duration: none;';
      if (currentPath.value != item.key) {
        style += '--n-text-color: #000';
      }
    } else {
      style = '--n-color: #18181CFF;--n-color-focus: #18181CFF;--n-color-hover: #18181CFF;';
      style += '--n-color-pressed: #18181CFF;--n-ripple-duration: none;';
      if (currentPath.value != item.key) {
        style += '--n-text-color: #bbbbbbs';
      }
    }
    return style;
  }

  /**
   * 菜单加载
   */
  const { menus } = storeToRefs(appStore);
  watch(menus, () => {
    const menu = appStore.expandMenus.find(i => i.key == currentPath.value);
    if (!menu) {
      // 找不到菜单时跳转到第一个选项卡
      if (appStore.visitedMenus.length > 0) {
        router.push(appStore.visitedMenus[0].key);
      } else {
        const simpleTitle = appStore.platformOption.platformSimpleTitle;
        document.title = simpleTitle ? simpleTitle : 'App';
      }
      return;
    }

    // 将当前选项卡加入路由（用于第一次登录系统或者浏览器手动输入地址）
    addVisitedRouter({...route});

    // 计算右键菜单选项
    getContextMenuOptions(false);

    // 滚动条溢出时滚动到当前选项卡
    // 由于选项卡加载时使用了动画（动画时长1s）导致元素位置不准确，所以做延时滚动
    setTimeout(() => {
      calScrollbarProps();
      scrollToCurrentTab();
    }, 1050);
  });

  /**
   * 路由进入前
   */
  router.beforeEach((to: RouteLocationNormalized) => {
    return addVisitedRouter(to);
  });
  function addVisitedRouter(to: RouteLocationNormalized) {
    if (routerHelper.isIgnoreRoute(to)) {
      return;
    }

    let menu = appStore.expandMenus.find(i => i.key == to.fullPath);
    if (appStore.visitedMenus.findIndex(i => i.key == to.fullPath) == -1) {
      // 没有菜单的界面（编辑、详情等等）
      if (!menu) {
        const parent = appStore.expandMenus.find(i => i.key == currentPath.value);
        if (!parent) {
          return false;
        }
        menu = {
          key: to.fullPath,
          label: parent.label,
          fullUrl: parent.fullUrl,
          icons: parent.icons,
          from: parent.from ? parent.from : parent.key
        }
        appStore.expandMenus.push(menu);
      }
      if (!menu.icons) {
        menu.icons = menu.parentIcon;
      }
      appStore.visitedMenus.push({...menu} as MenuOption);
    }
    currentPath.value = to.fullPath;
    changeDocumentTitle(menu.label);
    return true;
  }

  /**
   * 更改浏览器标题
   */
  function changeDocumentTitle(title: string) {
    const simpleTitle = appStore.platformOption.platformSimpleTitle;
    document.title = simpleTitle ? simpleTitle + ' | ' + title : title;
  }

  /********************************************************************************
   * 滚动条Start
   ********************************************************************************/

  /**
   * 计算滚动条属性（右侧滚动箭头是否可用）
   */
  function calScrollbarProps() {
    const tabItemElements = document.getElementsByClassName("tab-item");
    if (tabItemElements.length > 0) {

      // 获取滚动条总宽度（也就是最后一个元素的位置）
      const tabItemElement = tabItemElements[tabItemElements.length - 1] as HTMLElement;
      const scrollWidth = tabItemElement.offsetLeft;

      // 获取显示的容器宽度
      const clientWidth: number = tabItemElement.parentElement?.clientWidth!;

      // 滚动条属性(检测右滚动图标是否可用)
      scrollbarProps.value = {
        scrollLeft: 0,
        scrollWidth: scrollWidth,
        clientWidth: clientWidth
      }
    }
  }

  /**
   * 检查左右滚动图标是否显示(滚动条浮动允许为20px)
   */
  function isDisabledArrow() {
    const { scrollLeft, scrollWidth, clientWidth } = scrollbarProps.value;
    leftArrowDisabled.value = scrollLeft == 0;
    rightArrowDisabled.value = Math.round(scrollLeft + 20) >= (scrollWidth - clientWidth);
  }

  /**
   * 向左/右滚动
   */
  function arrowClick(isLeft: boolean) {
    const scroll = scrollbar.value as InstanceType<typeof NScrollbar>;
    const scrollLeft = scrollbarProps.value.scrollLeft;
    scroll.scrollTo({
      left: isLeft ? Math.max(0, scrollLeft - 400) : scrollLeft + 400,
      debounce: isLeft,
      behavior: 'smooth',
    } as any, 0);
  }

  /**
   * 滚动事件
   */
  function onScroll(e: any) {
    e.preventDefault();
    scrollbarProps.value = {
      scrollLeft: e.target.scrollLeft,
      scrollWidth: e.target.scrollWidth,
      clientWidth: e.target.clientWidth,
    };
  }

  /**
   * 侦听路由
   */
  watch(() => route.fullPath, () => {
    if (routerHelper.isIgnoreRoute(route)) {
      return;
    }

    // 路由改变，重新计算右键菜单并滚动到当前路由
    getContextMenuOptions(false);
    scrollToCurrentTab();
  });

  /**
   * 监听滚动条变化
   */
  watch(scrollbarProps, () => {
    isDisabledArrow();
  });

  /**
   * 侦听菜单折叠事件
   */
  let isManualCollapse = false;
  const {isCollapse} = storeToRefs(layoutStore);
  watch(isCollapse, () => {
    // 没有手动折叠过菜单并且当前折叠菜单
    if (!isManualCollapse && layoutStore.isCollapse) {
      isManualCollapse = true;
      return;
    }

    // 手动折叠过菜单并且当前打开菜单
    if (isManualCollapse && !layoutStore.isCollapse) {
      calScrollbarProps();

      // 滚动条溢出时滚动到当前选项卡
      // 由于折叠菜单加载时使用了动画（动画时长300ms）导致元素位置不准确，所以做延时滚动
      setTimeout(() => {
        scrollToCurrentTab();
      }, 350);
    }
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

    contextCurrentMenu.value = item;
    x.value = e.clientX;
    y.value = e.clientY;

    // 计算右键菜单选项
    getContextMenuOptions(true);

    // 已经打开时先关闭，实现动效
    if (showContextMenu.value) {
      showContextMenu.value = false;

      setTimeout(() => {
        showContextMenu.value = true;
      }, 100);
    } else {
      showContextMenu.value = true;
    }
  }

  /**
   * 关闭右键菜单
   */
  function contextMenuClickOutSide() {
    // 重新计算右键菜单选项
    getContextMenuOptions(false);
    showContextMenu.value = false;
  }

  /**
   * 右键菜单属性
   */
  function getContextMenuOptions(isMouseRight: boolean) {
    if (!isMouseRight) {
      contextCurrentMenu.value = appStore.expandMenus.find(i => i.key == currentPath.value);
    }

    contextMenuOptions.value = [{
      label: '内容全屏',
      key: 'fullscreen',
      icon() {
        return h(NIcon, null, {
          default: () => h(Expand),
        })
      },
    },
    //   {
    //   label: '刷新页面',
    //   key: 'refresh',
    //   icon() {
    //     return h(NIcon, null, {
    //       default: () => h(Refresh),
    //     })
    //   },
    // },
      {
      label: '关闭当前',
      key: 'closeCurrent',
      disabled: contextCurrentMenu.value.fixed || appStore.visitedMenus.length == 1,
      icon() {
        return h(NIcon, null, {
          default: () => h(Unlink),
        })
      },
    },{
      label: '关闭其他',
      key: 'closeOther',
      disabled: (closeLeftDisable() && closeRightDisable()) || appStore.visitedMenus.length == 1,
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
      disabled: appStore.visitedMenus.filter(i => !i.fixed).length <= 0 || appStore.visitedMenus.length == 1,
      icon() {
        return h(NIcon, null, {
          default: () => h(CloseOutline),
        })
      },
    }]
  }

  /**
   * 点击选项卡
   */
  function itemClick(item: MenuOption) {
    router.push(item.key);
  }

  /**
   * 触发选项
   */
  function onDropDownSelect(key: string) {
    if ("fullscreen" == key) {
      layoutStore.fullScreen();
    }
    if ("refresh" == key) {
      refreshRoute();
    }
    if ("closeCurrent" == key) {
      removeTab(contextCurrentMenu.value.key);
    }
    if ("closeOther" == key) {
      closeLeft();
      closeRight();
    }
    if ("closeLeft" == key) {
      closeLeft();
    }
    if ("closeRight" == key) {
      closeRight();
    }
    if ("closeAll" == key) {
      closeAll();
    }

    // 重新计算右键菜单选项
    showContextMenu.value = false;
    getContextMenuOptions(false);
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
    return appStore.visitedMenus.findIndex(i => i.key == contextCurrentMenu.value.key);
  }

  /**
   * 移除选项卡
   */
  function removeTab(path: string) {
    const index = appStore.visitedMenus.findIndex(i => i.key == path);
    appStore.visitedMenus.splice(index, 1);
    toLastTabMenu();
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
   * 关闭全部
   */
  function closeAll() {
    const menus = appStore.visitedMenus.filter(i => i.fixed);
    if (menus.length <= 0) {
      menus.push(appStore.visitedMenus[0]);
    }
    appStore.visitedMenus = menus;
    toLastTabMenu();
  }

  /**
   * 切换到最后一个菜单
   */
  function toLastTabMenu() {
    // 当前路由被关闭时才切换到最后一个选项卡
    if (appStore.visitedMenus.findIndex(i => i.key == currentPath.value) == -1) {
      router.push({ path: appStore.visitedMenus[appStore.visitedMenus.length -1].key });
      return;
    }

    // 未关闭时滚动到当前选项卡
    calScrollbarProps();
    setTimeout(() => {{
      scrollToCurrentTab();
    }}, 0);
  }

  /**
   * 刷新页面
   */
  function refreshRoute() {
    router.replace("/refresh/fresh?redirect=" + route.fullPath);
  }

  /**
   * 菜单图标
   *
   * @param icon
   */
  function renderMenuIcon(icon: string) {
    icon = !icon ? 'menu' : icon;
    return () => h(NIcon, null, { default: () => h(SvgIcon, {name: icon}) })
  }
</script>

<style scoped lang="scss">
  .vaw-tab-bar-container {
    display: flex;
    align-items: center;
    min-height: $tabHeight;
    padding: 0 10px;
    white-space: nowrap;
    .tab-item {
      padding: 6px 16px 4px;
      .icon-item {
        margin-left: 3px;
      }
      animation: left-to-right 1s cubic-bezier(0.175, 0.885, 0.32, 1.275);
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
  @keyframes left-to-right {
    from {
      transform: translateX(-100%);
    }
    to {
      transform: translateX(0);
    }
  }
</style>