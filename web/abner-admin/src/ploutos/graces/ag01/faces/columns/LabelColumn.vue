<template>
  <span @click="onTextClick" class="label">{{column.rowData[column.name]}}</span>
</template>

<script setup lang="ts">
  import {type PropType} from 'vue'
  import type TagColumnProps from "@/ploutos/graces/ag01/faces/columns/TagColumnProps.ts";
  import {message} from "@/ploutos";
  /********************************************************************************
   * 文本标签列
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
   * 文本点击
   */
  async function onTextClick() {
    await navigator.clipboard.writeText(props.column.rowData[props.column.name]);
    message.success("复制成功");
  }
</script>

<style scoped lang="scss">
.label {
  &:hover {
    color: var(--primary-color);
  }
}
</style>