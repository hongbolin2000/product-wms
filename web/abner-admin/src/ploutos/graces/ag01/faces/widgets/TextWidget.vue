<template>
  <n-input v-model:value="widget.rowData[widget.name]" :placeholder="'输入' + widget.title" :disabled="widget.disabled"/>
</template>

<script setup lang="ts">
  import type { PropType } from 'vue'
  import type AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";
  /********************************************************************************
   * 文本输入控件
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 父组件传入的属性
   */
  defineProps({
    widget: {
      type: Object as PropType<AbstractWidget>,
      required: true
    }
  });
</script>

<style scoped lang="scss">

</style>