<template>
  <n-tag :type="column.tagType">
    {{column.rowData[column.name]}}
  </n-tag>
</template>

<script setup lang="ts">
import {onBeforeMount, onBeforeUpdate, type PropType} from 'vue'
  import type TagColumnProps from "@/ploutos/graces/ag01/faces/columns/TagColumnProps.ts";
  /********************************************************************************
   * tag标签列
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    column: {
      type: Object as PropType<TagColumnProps>,
      required: true
    }
  });

  /**
   * 组件加载前
   */
  onBeforeMount(() => {
    tagType();
  });

  /**
   * 组件更新前
   */
  onBeforeUpdate(() => {
    tagType();
  });

  /**
   * 计算标签类型
   */
  function tagType() {
    const func = new Function( 'rowData', 'express', 'return eval("rowData." + express)');
    if (props.column.success && func(props.column.rowData, props.column.success)) {
      props.column.tagType = 'success';
      return;
    }
    if (props.column.warning && func(props.column.rowData, props.column.warning)) {
      props.column.tagType = 'warning';
      return;
    }
    if (props.column.error && func(props.column.rowData, props.column.error)) {
      props.column.tagType = 'error';
      return;
    }
    props.column.tagType = 'primary';
  }
</script>