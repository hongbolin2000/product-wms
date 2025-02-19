<template>
  <div class="action-items-wrapper">
    <n-button secondary v-if="layoutStore.navbar.isShowRefresh" @click="onRefreshRoute" class="action-item">
      <n-icon size="18">
        <RefreshIcon />
      </n-icon>
    </n-button>

    <n-button secondary v-if="layoutStore.navbar.isShowFullScreen" @click="onScreenFull" class="action-item">
      <n-icon size="18">
        <ExpandIcon />
      </n-icon>
    </n-button>

    <n-button secondary  @click="onShowSetting" class="action-item">
      <n-icon size="18">
        <SettingIcon />
      </n-icon>
    </n-button>

    <LayoutSetting ref="layoutSetting" />
  </div>
</template>

<script setup lang="ts">
import {Expand as ExpandIcon, RefreshOutline as RefreshIcon, SettingsOutline as SettingIcon,} from '@vicons/ionicons5'
import {ref} from "vue"
import {useRoute, useRouter} from 'vue-router'
import {useMessage} from 'naive-ui'
/********************************************************************************
 * 导航栏工具
 *
 * @author Berlin
 ********************************************************************************/
import useLayoutStore from "@/ploutos/layouts/store/layout-store"
import LayoutSetting from "@/ploutos/layouts/actions/setting/LayoutSetting.vue";
import {screen} from "@/ploutos";

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
  const layoutSetting = ref();

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
    screen.full();
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
    height: 100%;
    display: flex;
    align-items: center;
    .action-item {
      margin-right: 10px;
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
