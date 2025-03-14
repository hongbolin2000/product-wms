<template>
  <Graces.FormEditor
      module="ba01"
      name="storeEditor"
      @on-save="(value) => console.log(value)"
      :fill="!!id"
      :params="{storeId: id}"
  />
</template>

<script setup lang="ts" name="FormEditorSample">
  /********************************************************************************
   * 通用编辑表单示例界面
   *
   * @author Berlin
   ********************************************************************************/
  import {Graces} from '@/ploutos/index.ts';
  import {useRoute} from "vue-router";

  /**
   * 路由
   */
  const route = useRoute();

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    value: {
      type: String
    }
  });

  /**
   * id
   */
  const id = route.params.id || props.value;
</script>

<style scoped lang="scss">

</style>