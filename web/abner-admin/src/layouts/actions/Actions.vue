<template>
  <div class="action-items-wrapper">
    <span v-if="layoutStore.navbar.isShowSearch" class="action-item">
      <n-icon size="18">
        <SearchIcon />
      </n-icon>
    </span>

    <n-popover placement="bottom" trigger="click" :width="300">
      <template #trigger>
        <n-badge
          v-if="layoutStore.navbar.isShowMessage"
          :value="badgeValue"
          class="badge-action-item"
        >
          <n-icon size="18">
            <NotificationsIcon />
          </n-icon>
        </n-badge>
        <div v-else></div>
      </template>
      <NotifyMessage @clear="badgeValue = 0" />
    </n-popover>

    <span v-if="layoutStore.navbar.isShowRefresh" class="action-item" @click="onRefreshRoute">
      <n-icon size="18">
        <RefreshIcon />
      </n-icon>
    </span>

    <span v-if="layoutStore.navbar.isShowFullScreen" class="action-item" @click="onScreenFull">
      <n-icon size="18">
        <ExpandIcon />
      </n-icon>
    </span>

    <span class="action-item" @click="onShowSetting">
      <n-icon size="18">
        <SettingIcon />
      </n-icon>
    </span>

    <LayoutSetting ref="layoutSetting" />
  </div>
</template>

<script setup lang="ts">
  import {
    SearchOutline as SearchIcon, NotificationsOutline as NotificationsIcon,
    RefreshOutline as RefreshIcon, Expand as ExpandIcon,
    SettingsOutline as SettingIcon,
  } from '@vicons/ionicons5'
  import {ref} from "vue"
  import {useRoute, useRouter} from 'vue-router'
  import screenfull from 'screenfull'
  import { useMessage } from 'naive-ui'
  /********************************************************************************
   * 导航栏工具
   *
   * @author Berlin
   ********************************************************************************/
  import {useLayoutStore} from "@/layouts/store/layout-store"
  import NotifyMessage from "@/layouts/actions/message/NotifyMessage.vue";
  import LayoutSetting from "@/layouts/actions/setting/LayoutSetting.vue";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 当前路由
   */
  const route = useRoute();

  /**
   * 消息条数
   */
  const badgeValue = ref(3);

  /**
   * 提示消息
   */
  const message = useMessage();

  /**
   * 布局设置抽屉元素
   */
  const layoutSetting = ref(null);

  /**
   * 刷新路由
   */
  function onRefreshRoute() {
    router.push({ path: route.path, query: {t: new Date().getTime()} });
  }

  /**
   * 全屏
   */
  function onScreenFull() {
    if (!screenfull.isEnabled) {
      message.error('当前浏览器不支持全屏操作')
      return false
    }
    screenfull.toggle()
  }

  /**
   * 展开布局配置
   */
  function onShowSetting() {
    layoutSetting.value.openLayoutSettingDrawer();
  }
</script>

<style scoped lang="scss">
  .action-items-wrapper {
    position: relative;
    height: 100%;
    display: flex;
    align-items: center;
    z-index: 1;
    .action-item {
      min-width: 40px;
      display: flex;
      align-items: center;
      &:hover {
        cursor: pointer;
        color: var(--primary-color-hover);
      }
    }
    .badge-action-item {
      cursor: pointer;
      margin-right: 30px;
    }
  }
</style>
