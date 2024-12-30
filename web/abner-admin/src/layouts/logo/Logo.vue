<template>
  <div class="logo-wrapper">
    <img alt="logo" v-if="showLogo" class="logo-img" src="./logo.png" />
    <div
      v-if="showTitle"
      :class="[!layoutStore.isCollapse || alwaysShow ? 'show-title' : 'close-title']"
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
  import {useLayoutStore} from "@/layouts/store/layout-store";
  import {useAppStore} from "@/layouts/store/app-store";
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
      return 'calc((var(--menu-width)/3*2) - 40px)';
    } else if ((layoutStore.layoutMode === 'lcr')) {
      return 'calc(var(--menu-width) - 65px)';
    } else {
      return 'calc(var(--menu-width) - 40px)';
    }
  })
</script>

<style scoped lang="scss">
  .logo-wrapper {
    height: $logoHeight;
    display: flex;
    justify-content: center;
    align-items: center;
    border-bottom: 1px dashed var(--border-color);
    .logo-img {
      width: 30px;
    }
    .logo-title {
      font-weight: bold;
    }
    .show-title {
      transform: scale(1);
      width: auto;
      transition: transform 0.2s ease-in;
    }
    .close-title {
      transform: scale(0);
      width: 0;
    }
  }
</style>
