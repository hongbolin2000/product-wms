<template>
  <div class="action-items-wrapper">
    <n-tooltip trigger="hover">
      <template #trigger>
        <n-button secondary @click="onRefreshRoute" class="action-item">
          <n-icon size="18">
            <RefreshOutline />
          </n-icon>
        </n-button>
      </template>
      刷新
    </n-tooltip>

    <n-tooltip trigger="hover">
      <template #trigger>
        <n-button secondary @click="onScreenFull" class="action-item">
          <n-icon size="18">
            <Expand />
          </n-icon>
        </n-button>
      </template>
      全屏
    </n-tooltip>

    <n-tooltip trigger="hover">
      <template #trigger>
        <n-button secondary @click="onSwitchTheme" class="action-item">
          <n-icon size="18">
            <SunnyOutline />
          </n-icon>
        </n-button>
      </template>
      {{layoutStore.theme == ThemeMode.LIGHT ? '切换暗色模式' : '切换亮色模式'}}
    </n-tooltip>

    <n-tooltip trigger="hover">
      <template #trigger>
        <n-button secondary  @click="onShowSetting" class="action-item">
          <n-icon size="18">
            <SettingsOutline />
          </n-icon>
        </n-button>
      </template>
      布局设置
    </n-tooltip>
    <LayoutSetting ref="layoutSetting" />
  </div>
</template>

<script setup lang="ts">
import {Expand, RefreshOutline, SettingsOutline, SunnyOutline} from '@vicons/ionicons5'
import {ref} from "vue"
import {useRoute, useRouter} from 'vue-router'
/********************************************************************************
 * 导航栏工具
 *
 * @author Berlin
 ********************************************************************************/
import LayoutSetting from "@/ploutos/layouts/actions/setting/LayoutSetting.vue";
import {screen} from "@/ploutos";
import useLayoutStore from "@/ploutos/layouts/store/layout-store";
import {ThemeMode} from "@/ploutos/layouts/types";

/**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 当前路由
   */
  const route = useRoute();

  /**
   * 布局设置抽屉元素
   */
  const layoutSetting = ref();

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 刷新路由
   */
  function onRefreshRoute() {
    router.replace("/refresh/fresh?redirect=" + route.path);
  }

  /**
   * 全屏
   */
  function onScreenFull() {
    screen.full();
  }

  /**
   * 切换主题
   */
  function onSwitchTheme() {
    if (layoutStore.theme == ThemeMode.DARK) {
      layoutStore.theme = ThemeMode.LIGHT;
      layoutStore.themeBgColor = '#f0f2f5';
    } else {
      layoutStore.theme = ThemeMode.DARK;
      layoutStore.themeBgColor = '#101014FF';
    }
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
