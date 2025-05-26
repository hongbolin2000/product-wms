<template>
  <span @click="onTextClick" class="label">{{column.rowData[column.name]}}</span>
</template>

<script setup lang="ts">
  import {type PropType} from 'vue'
  import {message} from "@/ploutos";
  import clipboard3 from  'vue-clipboard3'
  /********************************************************************************
   * 文本标签列
   *
   * @author Berlin
   ********************************************************************************/
  import type TagColumnProps from "@/ploutos/graces/ag01/faces/columns/TagColumnProps.ts";

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
   * 剪切板
   */
  const { toClipboard } = clipboard3();

  /**
   * 文本点击
   */
  async function onTextClick() {
    await toClipboard(props.column.rowData[props.column.name]);
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