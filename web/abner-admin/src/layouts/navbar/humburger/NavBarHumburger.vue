<template>
  <span
    class="fold-wrapper iconfont"
    :class="[layoutStore.isCollapse ? 'fold-close-status' : 'fold-open-status']"
    @click="onToggle"
  >
    <MyIcon name="expand" type="svg"/>
  </span>
</template>

<script setup lang="ts">
  /********************************************************************************
   * 折叠菜单面包屑
   *
   * @author Berlin
   ********************************************************************************/
  import MyIcon from "@/icons/SvgIcon.vue";
  import {useLayoutStore} from "@/layouts/store/layout-store";

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
    box-sizing: border-box;
    display: inline-block;
    line-height: $logoHeight;
    text-align: center;
    padding: 0 10px;
    font-size: 22px;
    transition: transform $transitionTime;
  }
  .fold-wrapper:hover {
    color: #999999;
    cursor: pointer;
  }
</style>
