<template>
  <n-button secondary @click="onToggle"
      class="fold-wrapper iconfont"
      :class="[layoutStore.isCollapse ? 'fold-close-status' : 'fold-open-status']"
  >
    <MyIcon name="expand" type="svg"/>
  </n-button>
</template>

<script setup lang="ts">
  /********************************************************************************
   * 折叠菜单面包屑
   *
   * @author Berlin
   ********************************************************************************/
  import MyIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 折叠菜单
   */
  function onToggle() {
    layoutStore.isCollapse = !layoutStore.isCollapse;
  }
</script>

<style scoped lang="scss">
  .fold-open-status {
    transform: rotate(180deg);
  }
  .fold-close-status {
    transform: rotate(0);
  }
  .fold-wrapper {
    margin-right: 20px;
    margin-left: 20px;
    padding: 0 10px;
    font-size: 22px;
    transition: transform $transitionTime;
  }
  .fold-wrapper:hover {
    .svg-icon {
      fill: var(--primary-color-hover);
    }
  }
</style>
