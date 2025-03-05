<template>
  <div class="logo-wrapper" :style="logoStyle">
    <img alt="logo" v-if="showLogo" class="logo-img" src="./logo.png" />

    <div v-if="showTitle"
      :class="[layoutStore.isCollapse ? 'close-title' : 'show-title']"
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
  import {computed} from "vue";
  /********************************************************************************
   * Logo布局
   *
   * @author Berlin
   ********************************************************************************/
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import useAppStore from "@/ploutos/layouts/store/app-store";
  import {LayoutMode} from "@/ploutos/layouts/types";

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
    }
  });

  /**
   * logo样式
   */
  const logoStyle = computed(() => {
    if (layoutStore.layoutMode != LayoutMode.TopBottom) {
      return 'border-bottom: 1px dashed var(--border-color)';
    }
  })

  /**
   * 计算标题的最大显示宽度
   */
  const maxWidth = computed(() => {
    if (layoutStore.layoutMode === LayoutMode.TopBottom) {
      return 'calc(var(--menu-width) - 60px)';
    } else if ((layoutStore.layoutMode === LayoutMode.LeftSplit)) {
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
      margin-left: 10px;
      font-size: 18px;
    }
    .show-title {
      transform: scale(1);
      transition: transform $transitionTime;
    }
    .close-title {
      transform: scale(0);
      width: 0;
      transition: transform $transitionTime;
    }
  }
</style>
