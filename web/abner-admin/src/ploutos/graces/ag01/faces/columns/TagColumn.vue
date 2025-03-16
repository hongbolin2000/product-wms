<template>
  <n-tag :type="column.tagType">
    {{column.rowData[column.name]}}
  </n-tag>
</template>

<script setup lang="ts">
import {onBeforeMount, onBeforeUpdate, type PropType} from 'vue'
  import type TagColumnProps from "@/ploutos/graces/ag01/faces/columns/TagColumnProps.ts";
import {Parser} from "expr-eval";
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
    if (props.column.success && Parser.parse(props.column.success).evaluate(props.column.rowData)) {
      props.column.tagType = 'success';
      return;
    }
    if (props.column.warning && Parser.parse(props.column.warning).evaluate(props.column.rowData)) {
      props.column.tagType = 'warning';
      return;
    }
    if (props.column.error && Parser.parse(props.column.error).evaluate(props.column.rowData)) {
      props.column.tagType = 'error';
      return;
    }
    props.column.tagType = 'primary';
  }
</script>