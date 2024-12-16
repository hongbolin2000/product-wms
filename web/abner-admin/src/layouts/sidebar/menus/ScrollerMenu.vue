<template>
  <div class="scrollbar">
    <n-scrollbar>
      <n-menu
        mode="vertical"
        :value="currentPath"
        :collapsed="layoutStore.isCollapse"
        :options="menuOptions"
        :default-value="currentPath"
        :expanded-keys="expandKeys"
        :collapsed-icon-size="22"
        :collapsed-width="63"
        :indent="15"
        @update:value="onMenuClick"
        @update:expanded-keys="onMenuExpanded"
      />
    </n-scrollbar>
  </div>
</template>

<script setup lang="ts">
  import {h, ref, watch} from "vue"
  import {useRoute, useRouter} from "vue-router"
  import {type MenuOption, NIcon} from "naive-ui"
  import MyIcon from '@/icons/SvgIcon.vue';
  /********************************************************************************
   * @author Berlin
   ********************************************************************************/
  import {useLayoutStore} from "@/layouts/store/layout-store";
  import {DeviceType} from "@/layouts/types";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

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
  const currentPath = ref('');
  currentPath.value = currentRoute.fullPath

  /**
   * 展开的菜单
   */
  const expandKeys = ref(['']);
  handleExpandPath();

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
   * 测试路由
   */
  const menuOptions: MenuOption[] = [
    {
      label: 'Dashboard',
      key: '/index',
      icon: renderIcon("dashboard"),
      children: [{
        label: '主控台',
        key: '/index/home',
        icon: renderIcon(""),
      },{
        label: '工作台',
        key: '/index/work-place',
        icon: renderIcon(""),
      },]
    },
    {
      label: '系统管理',
      key: '/system',
      children: [],
      icon: renderIcon("setting")
    },
    {
      label: '列表页面',
      key: '/list',
      children: [],
      icon: renderIcon("detail")
    },
    {
      label: '表单页面',
      key: '/form',
      children: [],
      icon: renderIcon("file-text")
    },
    {
      label: '功能/组件',
      key: '/function',
      children: [],
      icon: renderIcon("appstore")
    },
    {
      label: '结果页面',
      key: '/result',
      children: [],
      icon: renderIcon("file-unknown")
    },
    {
      label: '编辑器',
      key: '/editor',
      children: [],
      icon: renderIcon("edit")
    },
    {
      label: '拖拽',
      key: '/tuozhai',
      children: [],
      icon: renderIcon("interation")
    },
    {
      label: '多级菜单',
      key: '/multiply',
      children: [],
      icon: renderIcon("Partition")
    },
    {
      label: '地图',
      key: '/map',
      children: [],
      icon: renderIcon("location")
    },
    {
      label: '项目依赖',
      key: '/dependence',
      icon: renderIcon("menu")
    }
  ]

  /**
   * 计算展开的菜单
   */
  function handleExpandPath() {

    // 分隔出每一级路由菜单
    const keys = currentPath.value.split('/');
    let expands: string[] = keys.filter((item) => !!item);

    // 将每一级路由菜单进行拼接
    expands = expands.reduce((prev: string, current: string) => {
      const lastItem = prev[prev.length - 1]
      if (!lastItem) {
        prev.push('/' + current)
      } else {
        prev.push(lastItem + '/' + current)
      }
      return prev
    }, [] as string[]);
    expandKeys.value = Array.from(new Set([...expandKeys.value, ...expands]));
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
    currentPath.value = value;
    handleExpandPath();
  });
</script>

<style scoped lang="scss">
  :deep(.n-menu .n-submenu .n-menu-item-content__icon) {
    font-size: 16px !important;
  }
  :deep(.n-menu .n-menu-item-content .n-menu-item-content__icon) {
    font-size: 16px !important;
  }
  :deep(.n-menu .n-menu-item) {
    margin-top: 0;
    margin-bottom: 5px;
  }
  :deep(.n-menu .n-menu-item::before) {
    left: 5px;
    right: 5px;
    border-radius: 5px;
  }
  :deep(.n-menu .n-menu-item:hover) {
    background-color: var(--item-color-active);
  }
  .scrollbar {
    height: calc(100vh - #{$logoHeight}) !important;
  }
</style>
