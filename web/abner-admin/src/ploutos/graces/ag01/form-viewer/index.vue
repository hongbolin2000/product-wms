<template>

</template>

<script setup lang="ts">
  import {ref, type Ref, shallowRef} from "vue";
  /********************************************************************************
   * 通用浏览表单界面
   *
   * @author Berlin
   ********************************************************************************/
  import useAppStore from "@/ploutos/layouts/store/app-store.ts";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store.ts";
  import type ViewerProps from "@/ploutos/graces/ag01/form-viewer/ViewerProps.ts";

  /**
   * 应用状态
   */
  const appStore = useAppStore();
  const layoutStore = useLayoutStore();

  /**
   * 是否加载中（提供给drawer和dialog使用）
   */
  const spining = shallowRef(false);

  /**
   * 浏览表单界面属性定义
   */
  const viewer: Ref<ViewerProps> = ref();


</script>

<style scoped lang="scss">

</style>