<template>
  <Graces.FormViewer :module="module" :name="name" :params="params"/>
</template>

<script setup lang="ts">
  import {useRoute} from "vue-router";
  import {onBeforeMount, type PropType, type Ref, shallowRef} from "vue";
  /********************************************************************************
   * 通用浏览表单加载页面
   *
   * @author Berlin
   ********************************************************************************/
  import {Graces} from '../index.ts';

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    params: {
      type: Object as PropType<{id: string, module: string, name: string}>
    }
  });

  /**
   * 当前路由
   */
  const route = useRoute();

  /**
   * 通用浏览表单定义模块号
   */
  const module = shallowRef('');

  /**
   * 通用浏览表单定义名称
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

    // 使用dialog或drawer模式
    if (props.params) {
      module.value = props.params.module;
      name.value = props.params.name;

      if (props.params && props.params.id) {
        params.value = {id: props.params.id};
      }
    }

    // 使用router跳转模式
    if (!props.params && route.params.id) {
      params.value = {id: route.params.id};
    }
  });
</script>

<style scoped lang="scss">

</style>