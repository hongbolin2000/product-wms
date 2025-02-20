<template>
  <div class="logo-wrapper" :style="layoutStore.layoutMode == 'ltr' ? 'border-bottom: 1px dashed var(--border-color)' : ''">
    <img alt="logo" v-if="showLogo" class="logo-img" src="./logo.png" />
    <div
      v-if="showTitle"
      :class="[!layoutStore.isCollapse ? 'show-title' : 'close-title']"
    >
      <span class="logo-title">
        <n-ellipsis :style="'max-width:' + maxWidth">
          {{ appStore.websiteOption.title }}
        </n-ellipsis>
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
  /********************************************************************************
   * Logo布局
   *
   * @author Berlin
   ********************************************************************************/
  import useLayoutStore from "../store/layout-store";
  import useAppStore from "../store/app-store";
  import {computed} from "vue";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 全局应用状态
   */
  const appStore = useAppStore();

  /**
   * 父组件传入的属性
   */
  defineProps({

    /**
     * 是否显示Logo图标
     */
    showLogo: {
      type: Boolean,
      default: true
    },

    /**
     * 是否显示网站标题
     */
    showTitle: {
      type: Boolean,
      default: true
    },

    /**
     * 总是显示网站标题
     */
    alwaysShow: Boolean
  });

  /**
   * 计算标题的最大显示宽度
   */
  const maxWidth = computed(() => {
    if (layoutStore.layoutMode === 'ttb') {
      return 'calc(var(--menu-width) - 60px)';
    } else if ((layoutStore.layoutMode === 'lcr')) {
      return 'calc(var(--tab-menu-width) - 70px)';
    } else {
      return 'calc(var(--menu-width) - 60px)';
    }
  })
</script>

<style scoped lang="scss">
  .logo-wrapper {
    height: $logoHeight;
    display: flex;
    justify-content: center;
    align-items: center;
    .logo-img {
      height: 32px;
    }
    .logo-title {
      font-weight: bold;
      color: #2d8cf0;
      margin-left: 12px;
      font-size: 18px;
    }
    .show-title {
      transform: scale(1);
      width: auto;
      transition: transform 0.2s ease-in;
    }
    .close-title {
      transform: scale(0);
      width: 0;
      transition: transform 0.2s ease-in;
    }
  }
</style>
