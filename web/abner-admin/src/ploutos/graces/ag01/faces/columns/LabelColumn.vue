<template>
    <span>{{column.rowData[column.name]}}</span>
</template>

<script setup lang="ts">
  import type { PropType } from 'vue'
  import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";
  /********************************************************************************
   * 标签列
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 父组件传入的属性
   */
  defineProps({
    column: {
      type: Object as PropType<AbstractColumn>,
      required: true
    }
  });
</script>

<style scoped lang="scss">

</style>