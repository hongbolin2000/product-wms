<template>
  <Graces.GridViewer :module="module" :name="name" :params="params"/>
</template>

<script setup lang="ts">
  import {useRoute} from "vue-router";
  /********************************************************************************
   * 通用浏览表格加载页面
   *
   * @author Berlin
   ********************************************************************************/
  import {Graces} from '../index.ts';
  import {onBeforeMount, type Ref, shallowRef} from "vue";

  /**
   * 当前路由
   */
  const route = useRoute();

  /**
   * 通用浏览表格定义模块号
   */
  const module = shallowRef('');

  /**
   * 通用浏览表格定义名称
   */
  const name = shallowRef('');

  /**
   * 参数
   */
  const params: Ref = shallowRef({});

  /**
   * 组件加载前
   */
  onBeforeMount(() => {
    module.value = route.params.module.toString();
    name.value = route.params.name.toString();
    if (route.query) {
      params.value = route.query;
    }
  });
</script>

<style scoped lang="scss">

</style>