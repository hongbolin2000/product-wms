<template>
  <div class="vaw-tab-bar-container" ref="tabBarContainer" v-if="appStore.visitedRoutes.length > 0">
    <div style="display: flex; align-items: center">
      <n-icon
          class="arrow-wrapper"
          :class="{ 'arrow-wrapper__disabled': leftArrowDisabled }"
          @click="arrowClick(true)"
      >
        <ChevronBack />
      </n-icon>

      <n-scrollbar ref="scrollbar" id="tab-bar-scrollbar" :x-scrollable="true" :size="0" :on-scroll="onScroll">
        <n-button
            v-for="item of appStore.visitedRoutes"
            :key="item.path"
            :type="currentPath === item.path ? 'primary' : 'default'"
            class="tab-item"
            strong
            secondary
            style="--n-height: 24px; --n-font-weight: 200"
            :data="item.path"
            @click.self="itemClick(item)"
            @contextmenu="onContextMenu(item, $event)"
        >
          <span style="font-size: 12px; margin-top: 2px" class="text-item" @click.self="itemClick(item)">
            {{ item.name }}
          </span>
          <n-icon class="icon-item" @click="removeTab(item)">
            <Close />
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

    <ul v-show="showContextMenu" class="context-menu-wrapper" :style="contextMenuStyle">
      <li>
        <n-button :underline="false" type="text" @click="refreshRoute">
          <template #icon>
            <n-icon>
              <Refresh />
            </n-icon>
          </template>
          刷新页面
        </n-button>
      </li>
      <li :disabled="showLeftMenu">
        <n-button :disabled="showLeftMenu" type="text" @click="closeLeft">
          <template #icon>
            <n-icon>
              <ArrowBack />
            </n-icon>
          </template>
          关闭左侧
        </n-button>
      </li>
      <li :disabled="showRightMenu">
        <n-button :disabled="showRightMenu" type="text" @click="closeRight">
          <template #icon>
            <n-icon>
              <ArrowForward />
            </n-icon>
          </template>
          关闭右侧
        </n-button>
      </li>
      <li>
        <n-button icon="el-icon-close" type="text" @click="closeAll">
          <template #icon>
            <n-icon>
              <Close />
            </n-icon>
          </template>
          关闭所有
        </n-button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
  import {ArrowBack, ArrowForward, ChevronBack, Close, Menu, Refresh} from '@vicons/ionicons5'
  import {h, ref, watch, onMounted, type Ref} from "vue";
  import {type RouteLocationNormalized, type RouteRecordRaw, useRoute, useRouter} from "vue-router";
  import {NIcon, NScrollbar} from "naive-ui";
  import useAppStore from "@/ploutos/layouts/store/app-store.ts";
  /********************************************************************************
   * 路由选项卡组件
   *
   * @author Berlin
   ********************************************************************************/

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
  const currentPath = ref(route.fullPath);

  /**
   * 容器元素
   */
  const scrollbar = ref();
  const tabBarContainer: Ref<Element> = ref({});

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
   * 右键选项左右偏移位
   */
  const contextMenuStyle = ref({
    left: '0',
    top: '0',
  });

  /**
   * 右键选项所选中的选项卡路由
   */
  const contextSelectRoute = ref();

  /**
   * 右键选项关闭左侧/右侧选项是否显示
   */
  const showContextMenu = ref(false);
  const showLeftMenu = ref(false);
  const showRightMenu = ref(false);

  /**
   * 组件加载
   */
  onMounted(() => {
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
      // 将当前路由加入选项卡
      addVisitedRouter({...route});
    }, 100);
  });

  /**
   * 右键选项
   */
  const contextMenuOptions = ref([
    {
      label: '刷新页面',
      key: 'refresh',
      icon() {
        return h(NIcon, null, {
          default: () => h(Refresh),
        })
      },
    },
    {
      label: '关闭所有',
      key: 'close',
      icon() {
        return h(NIcon, null, {
          default: () => h(Close),
        })
      },
    },
  ])

  /**
   * 路由进入前
   */
  router.beforeEach((to: RouteLocationNormalized) => {
    addVisitedRouter(to);
  });
  function addVisitedRouter(to: RouteLocationNormalized) {
    if (["/login", '/403', '/404'].includes(to.path)) {
      return;
    }
    if (appStore.visitedRoutes.findIndex(i => i.path === to.path) != -1) {
      isDisabledArrow();
      return;
    }
    const menuIndex = appStore.expandMenus.findIndex(i => i.key === to.path);
    if (menuIndex != -1) {
      to.name = appStore.expandMenus[menuIndex].label;
    }
    appStore.visitedRoutes.push(to as RouteRecordRaw);
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
    leftArrowDisabled.value = (scrollLeft === 0);
    rightArrowDisabled.value = Math.round(scrollLeft) === (scrollWidth - clientWidth);
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
  function onScroll(e) {
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
  watch(() => route.fullPath, () => {
    currentPath.value = route.fullPath;
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
  function onContextMenu(item: RouteRecordRaw, e: any) {
    e.preventDefault();
    contextSelectRoute.value = item;

    showLeftMenu.value = isLeftLast(item.path);
    showRightMenu.value = isRightLast(item.path);
    const { top, left } = e.target.getBoundingClientRect();

    contextMenuStyle.value.left = left +'px'
    contextMenuStyle.value.top = top + 'px'
    showContextMenu.value = true;
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
  }

  /**
   * 触发选项
   */
  function onDropDownSelect(key: string) {
    switch (key) {
      case 'refresh':
        refreshRoute()
        break
      case 'close':
        closeAll()
        break
    }
  }

  /**
   * 关闭左侧
   */
  function closeLeft() {
    const selectIndex = appStore.visitedRoutes.findIndex(i => i.path === contextSelectRoute.value.path);
    appStore.visitedRoutes = appStore.visitedRoutes.filter((item, index) => {
      return index >= selectIndex;
    });
  }

  /**
   * 关闭右侧
   */
  function closeRight() {
    const selectIndex = appStore.visitedRoutes.findIndex(i => i.path === contextSelectRoute.value.path);
    appStore.visitedRoutes = appStore.visitedRoutes.filter((item, index) => {
      return index <= selectIndex;
    });
  }

  /**
   * 关闭全部
   */
  function closeAll() {
    appStore.visitedRoutes = [];
  }

  /**
   * 刷新页面
   */
  function refreshRoute() {
    router.push({ path: route.path, query: {t: new Date().getTime()} });
  }

  /**
   * 检测左侧是否还有选项卡
   */
  function isLeftLast(currentPath: string) {
    const currentIndex = appStore.visitedRoutes.findIndex((it) => it.path === currentPath);
    return currentIndex === 0
  }

  /**
   * 检测右侧是否还有选项卡
   */
  function isRightLast(currentPath: string) {
    const currentIndex = appStore.visitedRoutes.findIndex((it) => it.path === currentPath);
    return currentIndex === appStore.visitedRoutes.length - 1
  }

  /**
   * 点击选项卡
   */
  function itemClick(item: RouteRecordRaw) {
    router.push(item.path);
  }

  /**
   * 移除选项卡
   */
  function removeTab(item: RouteRecordRaw) {
    const index = appStore.visitedRoutes.findIndex(i => i.path == item.path);
    appStore.visitedRoutes.splice(index, 1);
    if (item.path == currentPath.value) {
      router.push(appStore.visitedRoutes[appStore.visitedRoutes.length - 1].path);
    }
  }
</script>

<style scoped lang="scss">
  .vaw-tab-bar-container {
    position: relative;
    height: $tabHeight;
    line-height: calc(#{$tabHeight} - 3px);
    box-sizing: border-box;
    white-space: nowrap;
    box-shadow: 10px 5px 10px rgb(0 0 0 / 10%);
    .context-menu-wrapper {
      position: absolute;
      width: 130px;
      z-index: 999;
      list-style: none;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      background-color: var(--base-color);
      padding-left: 0;
      & > li {
        width: 100%;
        box-sizing: border-box;
        display: flex;
        align-items: center;
        padding: 5px 0;
      }
      & > li:hover {
        color: var(--primary-color);
      }
    }
    .humburger-wrapper {
      position: absolute;
      top: 0;
      left: 0;
      overflow: hidden;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100%;
    }
    .tab-humburger-wrapper {
      margin-left: 40px;
      transition: margin-left $transitionTime;
    }
    .tab-no-humburger-wrapper {
      margin-left: 0;
      transition: margin-left $transitionTime;
    }

    .tab-item {
      padding: 7px 10px;
      cursor: pointer;
      .icon-item {
        margin-left: 0;
        width: 0;
        height: 0;
        transition: all 0.2s ease-in-out;
        overflow: hidden;
      }
      &:hover {
        .icon-item {
          display: inline;
          width: 14px;
          height: 14px;
          margin-left: 5px;
          font-size: 12px;
          background-color: rgba(0, 0, 0, 0.12);
          border-radius: 50%;
          padding: 1px;
          transition: all 0.2s ease-in-out;
        }
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