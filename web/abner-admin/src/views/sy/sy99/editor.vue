<template>
  <Graces.FormEditor
      module="sy99"
      name="storeEditor"
      @on-save="(value) => console.log(value)"
      :fill="!!id"
      :params="{storeId: id}"
  />
</template>

<script setup lang="ts">
  /********************************************************************************
   * 通用编辑表单示例界面
   *
   * @author Berlin
   ********************************************************************************/
  import {Graces} from '@/ploutos';
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